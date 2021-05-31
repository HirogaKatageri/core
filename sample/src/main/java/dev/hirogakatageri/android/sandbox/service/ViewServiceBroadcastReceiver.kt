package dev.hirogakatageri.android.sandbox.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.hirogakatageri.android.sandbox.util.Broadcasts

class ViewServiceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val service = context as ViewService

        when (intent?.action) {
            Broadcasts.SERVICE_VIEW_ACTION_STOP -> service.stopSelf()
        }
    }
}