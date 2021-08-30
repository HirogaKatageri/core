package dev.hirogakatageri.sandbox.service.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.hirogakatageri.sandbox.service.SampleViewService
import dev.hirogakatageri.sandbox.util.Broadcasts

class ServiceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val service = context as SampleViewService

        when (intent?.action) {
            Broadcasts.SERVICE_VIEW_ACTION_STOP -> service.stopSelf()
        }
    }
}
