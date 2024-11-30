package com.example.faerntourism

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

@RequiresApi(Build.VERSION_CODES.O)
class DailyNotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(applicationContext, DAILY_NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Это интересно!")
            .setContentText("Донбеттыр - повелитель подводного царства...")
            .setOngoing(false)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(500, notification)
    }
}