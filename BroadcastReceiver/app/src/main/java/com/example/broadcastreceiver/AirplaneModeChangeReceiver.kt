package com.example.broadcastreceiver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class AirplaneModeChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                BluetoothAdapter.STATE_ON -> {
                    Toast.makeText(context, "Bluetooth is turned on", Toast.LENGTH_SHORT).show()
                }
                BluetoothAdapter.STATE_OFF -> {
                    Toast.makeText(context, "Bluetooth is turned Off", Toast.LENGTH_SHORT).show()                }
            }
        }
        val isAirplaneModeEnabled = intent?.getBooleanExtra("state", false) ?: return
        if (isAirplaneModeEnabled) {
            Toast.makeText(context, "Airplane Mode Enabled", Toast.LENGTH_SHORT).show()
            Log.d("TAG", "onReceive: Airplane Mode Enabled")
        } else {
            Toast.makeText(context, "Airplane Mode Disabled", Toast.LENGTH_SHORT).show()
            Log.d("TAG", "onReceive: Airplane Mode Enabled")
        }
    }

}
