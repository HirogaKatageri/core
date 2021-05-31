package dev.hirogakatageri.android.sandbox.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import dev.hirogakatageri.android.sandbox.App

const val SERVICE_VIEW_CHANNEL_ID = "9513e13e-8b16-41c9-922c-9483de674c81"

@RequiresApi(Build.VERSION_CODES.O)
fun App.createNotificationChannels() {
    val notificationManager: NotificationManager =
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    val channels = listOf(createServiceViewChannel())

    notificationManager.createNotificationChannels(channels)
}

fun App.deleteNotificationChannels() {
    val notificationManager: NotificationManager =
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    // Add Notification Channels to delete below...
}

@RequiresApi(Build.VERSION_CODES.O)
fun createServiceViewChannel(): NotificationChannel {
    val name = "Service View"
    val description = "Views that float outside the application."
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(SERVICE_VIEW_CHANNEL_ID, name, importance)
    channel.description = description
    channel.group

    return channel
}