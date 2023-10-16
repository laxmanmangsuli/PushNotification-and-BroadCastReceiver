@file:Suppress("DEPRECATION")

package com.example.broadcastreceiver

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.broadcastreceiver.databinding.ActivityMainBinding
import com.example.broadcastreceiver.foreground.ForegroundService
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    private lateinit var airPlaneMode: AirplaneModeChangeReceiver
    private lateinit var binding: ActivityMainBinding
    private val bluetoothAdapter: BluetoothAdapter? by lazy { BluetoothAdapter.getDefaultAdapter() }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("UnspecifiedRegisterReceiverFlag", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        askNotificationPermission()
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d("TAG", "onCreate: $token")

        }.addOnFailureListener { exception ->
            Log.d("TAG", "onCreate: ${exception.message.toString()}")
        }

        binding.btnOn.setOnClickListener {
            bluetoothAdapter?.enable()
            Toast.makeText(this, "Bluetooth is turned on", Toast.LENGTH_SHORT).show()
        }
        binding.btnOFF.setOnClickListener {
            bluetoothAdapter?.disable()
            Toast.makeText(this, "Bluetooth is turned Off", Toast.LENGTH_SHORT).show()
        }
        airPlaneMode = AirplaneModeChangeReceiver()

        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(airPlaneMode, it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airPlaneMode)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {binding.btnStartFore.setOnClickListener {

                ForegroundService.startService(this, "Foreground Service is running...")

            }
                binding.btnStopFore.setOnClickListener {
                    ForegroundService.stopService(this)
                }

            }
//            else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
//
//            }
            else {

                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
//            Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show()
        }
    }
}