package com.example.szakdolgozat

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.szakdolgozat.databinding.ActivityConnectBinding
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

class ConnectivityActivity : Activity() {
    private lateinit var binding : ActivityConnectBinding
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter : BluetoothAdapter
    private var bluetoothStatus = Status.DISABLED

    companion object {
        const val DEVICE_ADDRESS = "00:22:08:01:13:CD"
        val PORT_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
        var device: BluetoothDevice? = null
        var socket: BluetoothSocket? = null
        var outputStream: OutputStream? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConnectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBluetooth()

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.turnOnButton.setOnClickListener {
            turnOnBluetooth()
            bluetoothStatus = if (isBluetoothEnabled()){
                Status.ENABLED
            }else{
                Status.DISABLED
            }
            refreshBluetoothImage()
        }

        binding.connectToCarButton.setOnClickListener {
            bluetoothStatus = if(controlCar()){
                Status.CONNECTED
            } else{
                Status.ENABLED
            }
            refreshBluetoothImage()
        }

        binding.controlCarButton.setOnClickListener {
            if(/*connectToDevice()*/ true){
                intent = Intent(this, ButtonControlActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setUpBluetooth(){
        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        bluetoothStatus = if (isBluetoothEnabled()){
            Status.ENABLED
        }else{
            Status.DISABLED
        }
        refreshBluetoothImage()
    }

    private fun turnOnBluetooth(){
        if (!isBluetoothEnabled()) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        recreate()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun controlCar(): Boolean {
        bluetoothStatus = Status.SEARCHING
        refreshBluetoothImage()
        var found = false

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
            socket = device!!.createRfcommSocketToServiceRecord(PORT_UUID) //Creates a socket to handle the outgoing connection
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

    private fun refreshBluetoothImage(){
        when(bluetoothStatus){
            Status.DISABLED -> binding.bluetoothIconImageView.setImageResource(R.drawable.ic_bluetooth_disabled)
            Status.ENABLED -> binding.bluetoothIconImageView.setImageResource(R.drawable.ic_bluetooth_enabled)
            Status.SEARCHING -> binding.bluetoothIconImageView.setImageResource(R.drawable.ic_bluetooth_searching)
            Status.CONNECTED -> binding.bluetoothIconImageView.setImageResource(R.drawable.ic_bluetooth_connected)
        }
    }

    private fun isBluetoothEnabled() : Boolean{
        return bluetoothAdapter.isEnabled
    }
}