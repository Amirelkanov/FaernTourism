package com.amel.faerntourism.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.amel.faerntourism.FaernActivity
import com.amel.faerntourism.INTERESTING_PLACE_CHANNEL_ID
import com.amel.faerntourism.INTERESTING_PLACE_NOTIFICATION_CHANNEL_DESCRIPTION
import com.amel.faerntourism.INTERESTING_PLACE_NOTIFICATION_CHANNEL_NAME
import com.amel.faerntourism.INTERESTING_PLACE_NOTIFICATION_ID
import com.amel.faerntourism.INTERESTING_PLACE_NOTIFICATION_TITLE
import com.amel.faerntourism.R
import com.amel.faerntourism.REQUEST_CODE
import com.amel.faerntourism.SinglePlace
import com.amel.faerntourism.data.model.Place

@SuppressLint("MissingPermission")
fun makeInterestingPlaceNotification(
    place: Place,
    context: Context
) {
    createNotificationChannel(context)

    val pendingIntent = createNavigateToPlaceIntent(context, place.id)

    val builder = NotificationCompat.Builder(context, INTERESTING_PLACE_CHANNEL_ID)
        .setSmallIcon(R.drawable.landscape)
        .setContentTitle(INTERESTING_PLACE_NOTIFICATION_TITLE)
        .setContentText(place.name)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(context).notify(INTERESTING_PLACE_NOTIFICATION_ID, builder)
}

private fun createNotificationChannel(context: Context) {
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(
        INTERESTING_PLACE_CHANNEL_ID,
        INTERESTING_PLACE_NOTIFICATION_CHANNEL_NAME,
        importance
    )
    channel.description = INTERESTING_PLACE_NOTIFICATION_CHANNEL_DESCRIPTION

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

    notificationManager?.createNotificationChannel(channel)
}

fun createNavigateToPlaceIntent(appContext: Context, placeId: String): PendingIntent {
    val placeDeeplink = "faern://${SinglePlace.route}/$placeId".toUri()
    val routeIntent = Intent(appContext, FaernActivity::class.java).apply {
        data = placeDeeplink
    }

    val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

    return PendingIntent.getActivity(
        appContext,
        REQUEST_CODE,
        routeIntent,
        flags
    )
}

