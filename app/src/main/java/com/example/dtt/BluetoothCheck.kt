package com.example.dtt

import android.Manifest
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.view.View
import androidx.core.app.ActivityCompat
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions


class BluetoothCheck : Activity() {
    private val TAG = "BluetoothCheckTag"
    private lateinit var b1: Button
    private lateinit var b2: Button
    private lateinit var b3: Button
    private lateinit var b4: Button
    private lateinit var BA: BluetoothAdapter
    private lateinit var pairedDevices: Set<BluetoothDevice>
    private lateinit var lv: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_check)

        b1 = findViewById(R.id.button)
        b2 = findViewById(R.id.button2)
        b3 = findViewById(R.id.button3)
        b4 = findViewById(R.id.button4)

        val blueToothManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        BA = blueToothManager.adapter
        lv = findViewById(R.id.listView)
    }

    fun on(v: View) {
        if (!BA.isEnabled) {
            val turnOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    this, BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(this, arrayOf(BLUETOOTH_CONNECT), 0)
                return
            }
            startActivityForResult(turnOn, 0)
            Toast.makeText(applicationContext, "Turned on", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, "Already on", Toast.LENGTH_LONG).show()
        }
    }

    fun off(v: View) {
     /*   Log.e(TAG, "off: " + BA.isEnabled)
        if (BA.isEnabled) {
            // Disable Bluetooth using reflection
            try {
                // Get the BluetoothAdapter class
                val bluetoothAdapterClass = BA.javaClass

                // Get the disable method
                val disableMethod = bluetoothAdapterClass.getMethod("disable")

                // Invoke the disable method
                disableMethod.invoke(BA)

                Toast.makeText(applicationContext, "Bluetooth turned off", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                // Handle exception if unable to disable Bluetooth using reflection
                e.printStackTrace()
                Toast.makeText(applicationContext, "Failed to turn off Bluetooth", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(applicationContext, "Bluetooth is already turned off", Toast.LENGTH_LONG).show()
        }*/
        // Check if Bluetooth is currently enabled
        if (BA.isEnabled) {
            // Prompt the user to manually turn off Bluetooth
            val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
            startActivityForResult(intent, 0)
        } else {
            Toast.makeText(applicationContext, "Bluetooth is already turned off", Toast.LENGTH_LONG).show()
        }
        }



    fun visible(v: View) {
        val getVisible = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        startActivityForResult(getVisible, 0)
        if (ActivityCompat.checkSelfPermission(
                this, BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(this, arrayOf(BLUETOOTH_ADMIN), 0)
            return
        }

    }

    fun list(v: View) {
        if (ActivityCompat.checkSelfPermission(
                this, BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        pairedDevices = BA.bondedDevices

        val list = ArrayList<String>()
        for (bt in pairedDevices) list.add(bt.name)
        Toast.makeText(applicationContext, "Showing Paired Devices", Toast.LENGTH_SHORT).show()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        lv.adapter = adapter
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            0 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "onRequestPermissionsResult: "+"Granted !!! ")
                    BA.disable()
                    // Permission granted, you can call the corresponding function again
                    // For example, if you requested BLUETOOTH_ADMIN permission, you can call off() or visible() again
                } else {
                    // Permission denied, handle the error or show a message to the user
                    Toast.makeText(
                        applicationContext,
                        "Permission denied. Please grant the necessary permissions.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}
