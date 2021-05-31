package dev.hirogakatageri.android.sandbox.util

import android.app.Notification
import androidx.core.app.NotificationCompat
import dev.hirogakatageri.android.sandbox.R
import dev.hirogakatageri.android.sandbox.service.ViewService

fun ViewService.buildNotification(): Notification = NotificationCompat.Builder(
    this,
    SERVICE_VIEW_CHANNEL_ID
)
    .setSmallIcon(R.drawable.ic_view_service)
    .setContentTitle("View Service")
    .setContentText("Views are floating outside the application.")
    .setOngoing(true)
    .setAutoCancel(false)
    .setPriority(NotificationCompat.PRIORITY_HIGH)
    .addAction(
        R.drawable.ic_close,
        getString(R.string.service_view_action_stop_service),
        stopServiceViewIntent
    )
    .build()