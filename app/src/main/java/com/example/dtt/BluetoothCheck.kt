package com.example.dtt

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

class BluetoothCheck : AppCompatActivity() {

    private val REQUEST_ENABLE_BLUETOOTH = 1
    private val PERMISSION_REQUEST_CODE = 2

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var device: BluetoothDevice? = null
    private var socket: BluetoothSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_check)

       val btn_start_discovery = findViewById<Button>(R.id.btn_start_discovery)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        // Check if Bluetooth is supported on the device
        if (bluetoothAdapter == null) {
            // Bluetooth is not supported on this device
            return
        }

        // Check if Bluetooth is enabled, if not, prompt the user to enable it
        if (!bluetoothAdapter.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        } else {
            // Bluetooth is enabled, proceed with Bluetooth operations
            setupBluetooth()
        }

        // Set click listener for the button
        btn_start_discovery.setOnClickListener {
            // Check for Bluetooth permissions, if not granted, request for permissions
            if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                // Permissions are already granted, proceed with Bluetooth operations
                startDeviceDiscovery()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH && resultCode == RESULT_OK) {
            // Bluetooth is enabled by the user, proceed with Bluetooth operations
            setupBluetooth()
        }
    }

    private fun setupBluetooth() {
        // Do any setup needed for Bluetooth
    }

    private fun startDeviceDiscovery() {
        // Start device discovery
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        bluetoothAdapter.startDiscovery()

        // Register a BroadcastReceiver to receive the results of the discovery process
        val discoveryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action
                if (BluetoothDevice.ACTION_FOUND == action) {
// Device found during discovery
                    val device =
                        intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
// Do something with the discovered device
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
// Discovery process finished
// Do something when the discovery process is finished
                }
            }
        }
        // Register the BroadcastReceiver to listen for the results of the discovery process
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(discoveryReceiver, filter)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, proceed with Bluetooth operations
            startDeviceDiscovery()
        } else {
            // Permission denied, handle accordingly
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Unregister the BroadcastReceiver when the activity is destroyed
      //  unregisterReceiver(discoveryReceiver)

        // Close the Bluetooth socket and streams when the activity is destroyed
        try {
            socket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }





}