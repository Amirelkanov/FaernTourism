package com.amel.faerntourism.service

import android.annotation.SuppressLint
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.amel.faerntourism.FCM_CHANNEL_ID
import com.amel.faerntourism.FCM_NOTIFICATION_ID
import com.amel.faerntourism.R
import com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseMessageReceiver : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            showNotification(
                remoteMessage.notification!!.title,
                remoteMessage.notification!!.body
            )
        }
    }

    @SuppressLint("MissingPermission")
    fun showNotification(
        title: String?,
        message: String?
    ) {
        val pendingIntent = createPendingIntent(this)

        val builder = NotificationCompat.Builder(applicationContext, FCM_CHANNEL_ID)
            .setSmallIcon(R.drawable.landscape)
            .setContentTitle(title)
            .setContentText(message)
            .setVibrate(LongArray(0))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()


        NotificationManagerCompat.from(this).notify(FCM_NOTIFICATION_ID, builder)
    }
}
