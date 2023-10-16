package com.example.broadcastreceiver.foreground

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.broadcastreceiver.MainActivity
import com.example.broadcastreceiver.R
import com.example.broadcastreceiver.WebViewActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

val channelId = "Notification_ID"
val channelName = "Notification_Name"

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TAG", "onNewToken: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()) {
            val url = remoteMessage.data["Key"]
            Log.d("TAG", "onMessageReceived URI: $url")

            val intent = if (!url.isNullOrEmpty()) {
                val uri = Uri.parse(url)
                Intent(Intent.ACTION_VIEW, uri)
            } else {
                Intent(this, MainActivity::class.java)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = remoteMessage.notification

            if (notification != null) {
                buildAndShowNotification(notification, pendingIntent)
            }
        }
    }

    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)

        if (intent?.action == "com.google.firebase.MESSAGING_EVENT") {
            val remoteMessage = intent.getParcelableExtra<RemoteMessage>("com.google.firebase.MESSAGING_EVENT")
            remoteMessage?.data?.let { data ->
                val url = data["Key"]
                Log.d("TAG", "handleIntent URI: ${url.toString()}")

                val intent = if (!url.isNullOrEmpty()) {
                    val uri = Uri.parse(url)
                    Intent(Intent.ACTION_VIEW, uri)
                } else {
                    Intent(this, MainActivity::class.java)
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

                val pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                )

                val notification = remoteMessage.notification

                if (notification != null) {
                    buildAndShowNotification(notification, pendingIntent)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun buildAndShowNotification(
        message: RemoteMessage.Notification,
        pendingIntent: PendingIntent
    ) {
        val channelId = "channel_id"
        val channelName = "Channel Name"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_android)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOngoing(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notificationManagerCompact = NotificationManagerCompat.from(this)

        notificationManagerCompact.notify(0, notificationBuilder.build())
    }
}






















//
//class MyFirebaseMessagingService : FirebaseMessagingService() {
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//        Log.d("TAG", "onNewToken: $token")
//    }
//
//    override fun handleIntent(intent: Intent?) {
//        super.handleIntent(intent)
//
//        if (intent?.action == "com.google.firebase.MESSAGING_EVENT") {
//            val remoteMessage = intent.getParcelableExtra<RemoteMessage>("com.google.firebase.MESSAGING_EVENT")
//            remoteMessage?.data?.let { data ->
//                val url = data["Key"]
//                Log.d("TAG", "handleIntent URI: ${url.toString()}")
//
//                val intent = if (!url.isNullOrEmpty()) {
//                    val uri = Uri.parse(url)
//                    Intent(Intent.ACTION_VIEW, uri)
//                } else {
//                    Intent(this, MainActivity::class.java)
//                }
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
//
//                val pendingIntent = PendingIntent.getActivity(
//                    this,
//                    0,
//                    intent,
//                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
//                )
//
//                val notification = remoteMessage.notification
//
//                if (notification != null) {
//                    buildAndShowNotification(notification, pendingIntent)
//                }
//            }
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun buildAndShowNotification(
//        message: RemoteMessage.Notification,
//        pendingIntent: PendingIntent
//    ) {
//        val channelId = "channel_id"
//        val channelName = "Channel Name"
//
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.ic_android)
//            .setContentTitle(message.title)
//            .setContentText(message.body)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setAutoCancel(true)
//            .setOngoing(true)
//            .setContentIntent(pendingIntent)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                channelName,
//                NotificationManager.IMPORTANCE_HIGH
//            )
//
//            val notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        val notificationManagerCompact = NotificationManagerCompat.from(this)
//
//        notificationManagerCompact.notify(0, notificationBuilder.build())
//    }
//}












//class MyFirebaseMessagingService : FirebaseMessagingService() {
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//        Log.d("TAG", "onNewToken: $token")
//    }
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
//        Log.d("TAG", "onMessageReceived: ${remoteMessage.data}")
//
//        val url = remoteMessage.data["Key"]
//        Log.d("TAG", "onMessageReceived URI: ${url.toString()} ")
//        if (url != null) {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//            val pendingIntent = PendingIntent.getActivity(
//                this,
//                0,
//                intent,
//                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
//            )
//            val notification = remoteMessage.notification
//            if (notification != null) {
//                buildAndShowNotification(notification, pendingIntent)
//            }
//        }
//    }
//
////    override fun onMessageReceived(remoteMessage: RemoteMessage) {
////        super.onMessageReceived(remoteMessage)
////        Log.d("TAG", "onMessageReceived: ${remoteMessage.data.toString()}")
////        remoteMessage.notification?.let { notification ->
////            buildAndShowNotification(notification)
////        }
////    }
//
//    @SuppressLint("MissingPermission")
//    private fun buildAndShowNotification( message: RemoteMessage.Notification,
//                                          pendingIntent: PendingIntent) {
////        val notificationBuilder = NotificationCompat.Builder(this, channelId)
////            .setSmallIcon(R.drawable.ic_android)
////            .setContentTitle(message.title)
////            .setContentText(message.body)
////            .setPriority(NotificationCompat.PRIORITY_HIGH)
////            .setAutoCancel(true)
////            .setOngoing(true)
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.ic_android)
//            .setContentTitle(message.title)
//            .setContentText(message.body)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setAutoCancel(true)
//            .setOngoing(true)
//            .setContentIntent(pendingIntent)
//
////        val intent = Intent(this, MainActivity::class.java)
////
////        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
////        notificationBuilder.setContentIntent(pendingIntent)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
//
//            val notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//        val notificationManagerCompact = NotificationManagerCompat.from(this)
//        notificationManagerCompact.notify(0, notificationBuilder.build())
//
//
//    }
//}
