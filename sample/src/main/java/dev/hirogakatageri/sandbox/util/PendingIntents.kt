package dev.hirogakatageri.sandbox.util

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent

val Context.stopServiceViewIntent: PendingIntent
    get() = PendingIntent.getBroadcast(
        this,
        0x792cbebd,
        Intent().apply {
            action = Broadcasts.SERVICE_VIEW_ACTION_STOP
        },
        FLAG_ONE_SHOT
    )
