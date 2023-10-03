package com.example.szakdolgozat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.szakdolgozat.databinding.ActivityButtonControlBinding
import java.io.IOException

class ButtonControlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityButtonControlBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityButtonControlBinding.inflate(layoutInflater)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(binding.root)

        binding.upButton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                sendForwardCommand()
            } else if (event.action == MotionEvent.ACTION_UP) {
                sendDefaultCommand()
            }
            false
        }
    }



    private fun sendCommand(command: String){
        try {
            ConnectivityActivity.outputStream?.write(command.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun sendDefaultCommand(){
        sendCommand("D")
    }

    private fun sendForwardCommand(){
        sendCommand("F")
    }

    private fun sendBackwardCommand(){
        sendCommand("B")
    }

    private fun sendRightCommand(){
        sendCommand("R")
    }

    private fun sendLeftCommand(){
        sendCommand("L")
    }

}