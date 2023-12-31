package com.example.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val employeeDao = (application as EmployeeApp).db.employeeDao()
        binding.submitBtn.setOnClickListener {
            addRecord(employeeDao)
        }
        val adapter = ItemAdapter(ArrayList())
        binding.items.adapter = adapter
        lifecycleScope.launch {
            val employees = employeeDao.fetchAllEmployees().collect {
                binding.items.layoutManager = LinearLayoutManager(this@MainActivity)
                binding.items.adapter = ItemAdapter(ArrayList(it))
            }

        }

    }

    fun addRecord(employeeDao: EmployeeDAO) {
        val name = binding.tName.text.toString()
        val email = binding.tEmail.text.toString()
        if (name.isNotEmpty() && email.isNotEmpty()) {
            lifecycleScope.launch {
                employeeDao.insert(EmployeeEntity(name = name, email = email))
                Toast.makeText(this@MainActivity, "Record Saved", Toast.LENGTH_LONG).show()
                binding.tName.text?.clear()
                binding.tEmail.text?.clear()
            }
        }
    }
}

