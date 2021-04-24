package com.example.myapplication.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.Secure.getString
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.myapplication.R
import com.example.myapplication.ui.setView.SetViewActivity

class NotificationReceiver : BroadcastReceiver() {
    private val REQUEST_CODE = 100
    private val CHANNEL_ID = "CHANNEL_ID"
    private val NOTIFICATION_ID = 2002
    override fun onReceive(p0: Context?, p1: Intent?) {
        val notificationManager = p0?.getSystemService(Context.NOTIFICATION_SERVICE)

        val intent = Intent(p0,SetViewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

       val pendingIntent = PendingIntent.getActivity(p0,REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(p0!!,CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Пора заниматься")
            .setContentText("Время заниматься пришло")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(p0)) {
            // notificationId is a unique int for each notification that you must define
            notify(NOTIFICATION_ID, builder.build())
        }



    }



}
