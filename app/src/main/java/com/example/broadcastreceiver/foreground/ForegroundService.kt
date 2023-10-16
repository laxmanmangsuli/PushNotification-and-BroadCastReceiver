package com.example.broadcastreceiver.foreground

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.broadcastreceiver.MainActivity
import com.example.broadcastreceiver.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ForegroundService : Service() {
    private val CHANNEL_ID = "ForegroundService Kotlin"

//    class MyFirebaseMessagingService : FirebaseMessagingService() {
//        override fun onNewToken(token: String) {
//            super.onNewToken(token)
//            Log.d("TAG", "onNewToken: $token")
//        }
//
//        override fun onMessageReceived(remoteMessage: RemoteMessage) {
//            super.onMessageReceived(remoteMessage)
//            Log.d("TAG", "onMessageReceived: ${remoteMessage.data}")
//
//            val url = remoteMessage.data["Key"]
//            Log.d("TAG", "onMessageReceived URI: ${url.toString()}")
//            if (url != null) {
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Ensure the intent opens the URL in a new task
//
//                val pendingIntent = PendingIntent.getActivity(
//                    this,
//                    0,
//                    intent,
//                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT // Use FLAG_UPDATE_CURRENT
//                )
//
//                val notification = remoteMessage.notification
//                if (notification != null) {
//                    buildAndShowNotification(notification, pendingIntent)
//                }
//            }
//        }
//
//        @SuppressLint("MissingPermission")
//        private fun buildAndShowNotification(
//            message: RemoteMessage.Notification,
//            pendingIntent: PendingIntent
//        ) {
//            val channelId = "channel_id"
//            val channelName = "Channel Name"
//
//            val notificationBuilder = NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.drawable.ic_android)
//                .setContentTitle(message.title)
//                .setContentText(message.body)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true)
//                .setOngoing(true)
//                .setContentIntent(pendingIntent)
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val channel = NotificationChannel(
//                    channelId,
//                    channelName,
//                    NotificationManager.IMPORTANCE_HIGH
//                )
//
//                val notificationManager =
//                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                notificationManager.createNotificationChannel(channel)
//            }
//
//            val notificationManagerCompact = NotificationManagerCompat.from(this)
//            notificationManagerCompact.notify(0, notificationBuilder.build())
//        }
//    }



//    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MyActivity.this,  new OnSuccessListener<InstanceIdResult>() {
//        @Override
//        public void onSuccess(InstanceIdResult instanceIdResult) {
//            String newToken = instanceIdResult.getToken();
//            Log.e("newToken",newToken);
//
//        }
//    });
    companion object {
        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, ForegroundService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }
        fun stopService(context: Context) {
            val stopIntent = Intent(context, ForegroundService::class.java)
            context.stopService(stopIntent)
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val input = intent?.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
            0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service Kotlin Example")
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
        startForeground(1, notification)
        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_HIGH)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

//    class MyFirebaseMessagingService : FirebaseMessagingService() {
//        override fun onMessageReceived(remoteMessage: RemoteMessage) {
//            Log.d("TAG", "onMessageReceived: ${remoteMessage.toString()}")
//        }
//    }
}