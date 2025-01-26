package com.amel.faerntourism.models.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.amel.faerntourism.LOCATION_NOTIFICATION_CHANNEL_ID
import com.amel.faerntourism.R
import com.amel.faerntourism.models.LocationClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit


// TODO: на самом деле, я бы это тоже в воркер засунул:
//  типа время от времени просто чекать локацию пользователя (раз в час, скажем)
class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = LocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, LOCATION_NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Ищем интересные места поблизости...")
            .setSmallIcon(R.drawable.google_icon)
            .setOngoing(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(1.minutes.toLong(DurationUnit.MILLISECONDS))
            .catch { e ->
                e.printStackTrace()
            }
            .onEach { location ->
                val (lat, long) = location.latitude to location.longitude

                if (SphericalUtil.computeDistanceBetween(
                        LatLng(lat, long),
                        getClosestPlace(LatLng(lat, long))
                    ) <= 50
                ) {
                    val updatedNotification = notification
                        .setContentTitle("Нашли кое-что интересное для вас")
                        .setContentText("Пока что это вы! :)")
                        .setOngoing(false)
                    notificationManager.notify(1, updatedNotification.build())
                }

            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    // TODO: Заглушка на будущее (возможно вообще в другое место перекочует)
    private fun getClosestPlace(selfCoordinates: LatLng): LatLng {
        return selfCoordinates
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}