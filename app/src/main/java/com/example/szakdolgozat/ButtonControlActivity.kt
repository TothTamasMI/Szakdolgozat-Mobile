package com.example.szakdolgozat

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.szakdolgozat.databinding.ActivityButtonControlBinding
import java.io.IOException
import java.io.OutputStream
import java.util.UUID


class ButtonControlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityButtonControlBinding

    companion object {
        private const val DEVICE_ADDRESS = "00:22:08:01:13:CD"
        private val PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
        private var device: BluetoothDevice? = null
        private var socket: BluetoothSocket? = null
        private var outputStream: OutputStream? = null
    }

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

        binding.leftButton.setOnClickListener {
            if (setUpBluetooth()) {
                connectToDevice()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setUpBluetooth(): Boolean {
        var found = false
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(
                applicationContext,
                "Device doesn't support bluetooth",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (!bluetoothAdapter!!.isEnabled) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            val enableAdapter = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            )
            startActivityForResult(enableAdapter, 0)
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        val bondedDevices = bluetoothAdapter.bondedDevices
        if (bondedDevices.isEmpty()) //Checks for paired bluetooth devices
        {
            Toast.makeText(applicationContext, "Please pair the device first", Toast.LENGTH_SHORT)
                .show()
        } else {
            for (iterator in bondedDevices) {
                if (iterator.address == DEVICE_ADDRESS) {
                    device = iterator
                    found = true
                    break
                }
            }
        }
        return found
    }

    private fun connectToDevice(): Boolean {
        var connected = true
        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            }
            socket =
                device!!.createRfcommSocketToServiceRecord(PORT_UUID) //Creates a socket to handle the outgoing connection
            socket?.connect()
            Toast.makeText(
                applicationContext,
                "Connection to bluetooth device successful", Toast.LENGTH_LONG
            ).show()
        } catch (e: IOException) {
            e.printStackTrace()
            connected = false
        }
        if (connected) {
            try {
                outputStream = socket!!.outputStream //gets the output stream of the socket
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return connected
    }

    private fun sendCommand(command: String){
        try {
            outputStream?.write(command.toByteArray())
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