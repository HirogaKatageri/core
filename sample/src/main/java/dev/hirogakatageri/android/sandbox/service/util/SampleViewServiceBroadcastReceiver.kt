package dev.hirogakatageri.android.sandbox.service.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.hirogakatageri.android.sandbox.service.SampleViewService
import dev.hirogakatageri.android.sandbox.util.Broadcasts

class SampleViewServiceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val service = context as SampleViewService

        when (intent?.action) {
            Broadcasts.SERVICE_VIEW_ACTION_STOP -> service.stopSelf()
        }
    }
}