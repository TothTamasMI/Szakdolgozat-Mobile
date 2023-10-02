package com.example.szakdolgozat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.szakdolgozat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.connectButton.setOnClickListener {
            val intent = Intent(this, ConnectivityActivity::class.java)
            startActivity(intent)
        }

        binding.controlButton.setOnClickListener {
            val intent = Intent(this, ButtonControlActivity::class.java)
            startActivity(intent)
        }
    }
}