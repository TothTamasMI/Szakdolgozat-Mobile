package com.example.szakdolgozat

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.szakdolgozat.databinding.ActivityConnectBinding

class ConnectivityActivity : Activity() {

    private lateinit var binding : ActivityConnectBinding
    private var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var pairedDevices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1

    companion object {
        val EXTRA_ADDRESS: String = "Device_address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(bluetoothAdapter == null){
            Toast.makeText(this,"This device doesn't support bluetooth", Toast.LENGTH_LONG).show()
            return
        }
        if(!bluetoothAdapter!!.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        binding.searchForDevicesButton.setOnClickListener {
            pairedDeviceList()
        }
    }

    private fun pairedDeviceList(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        pairedDevices = bluetoothAdapter!!.bondedDevices
        val list : ArrayList<BluetoothDevice> = ArrayList()

        if(pairedDevices.isNotEmpty()){
            for (device : BluetoothDevice in pairedDevices){
                list.add(device)
            }
        }else{
            Toast.makeText(this,"No paired bluetooth devices found", Toast.LENGTH_LONG).show()
        }

        val adapter = ArrayAdapter(this, R.layout.list_item, list)
        binding.availableDevicesList.adapter = adapter
        binding.availableDevicesList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device : BluetoothDevice = list[position]
            val address : String = device.address

            val intent = Intent(this, ButtonControlActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH){
            if (resultCode == Activity.RESULT_OK){
                if(bluetoothAdapter!!.isEnabled){
                    Toast.makeText(this,"Bluetooth enabled", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this,"Bluetooth disabled", Toast.LENGTH_LONG).show()
                }
            }else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this,"Bluetooth enabling canceled", Toast.LENGTH_LONG).show()
            }
        }
    }

}