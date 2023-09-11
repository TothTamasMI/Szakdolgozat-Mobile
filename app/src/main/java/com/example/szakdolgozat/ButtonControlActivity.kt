package com.example.szakdolgozat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.szakdolgozat.databinding.ActivityButtonControlBinding

class ButtonControlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityButtonControlBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityButtonControlBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}