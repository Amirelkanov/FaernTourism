package com.amel.faerntourism.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.amel.faerntourism.FCM_CHANNEL_ID
import com.amel.faerntourism.FCM_NOTIFICATION_CHANNEL_DESCRIPTION
import com.amel.faerntourism.FCM_NOTIFICATION_CHANNEL_NAME
import com.amel.faerntourism.FaernActivity
import com.amel.faerntourism.REQUEST_CODE

@RequiresApi(Build.VERSION_CODES.O)
fun createFcmNotificationChannel(context: Context) {
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(
        FCM_CHANNEL_ID,
        FCM_NOTIFICATION_CHANNEL_NAME,
        importance
    )
    channel.description = FCM_NOTIFICATION_CHANNEL_DESCRIPTION

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

    notificationManager?.createNotificationChannel(channel)
}


fun createPendingIntent(appContext: Context): PendingIntent {
    val intent = Intent(appContext, FaernActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

    return PendingIntent.getActivity(
        appContext,
        REQUEST_CODE,
        intent,
        flags
    )
}