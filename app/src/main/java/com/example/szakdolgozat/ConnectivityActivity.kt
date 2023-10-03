package com.example.szakdolgozat

import android.app.Activity
import android.os.Bundle
import com.example.szakdolgozat.databinding.ActivityConnectBinding

class ConnectivityActivity : Activity() {

    private lateinit var binding : ActivityConnectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}