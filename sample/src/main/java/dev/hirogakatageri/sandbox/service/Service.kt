package dev.hirogakatageri.sandbox.service

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Build
import androidx.lifecycle.lifecycleScope
import dev.hirogakatageri.sandbox.service.ui.profile.ProfileView
import dev.hirogakatageri.sandbox.service.util.ServiceBroadcastReceiver
import dev.hirogakatageri.sandbox.util.Broadcasts
import dev.hirogakatageri.sandbox.util.buildNotification
import dev.hirogakatageri.viewservice.service.CoreViewService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

private const val NOTIFICATION_ID: Int = 0x2a93a245

class SampleViewService : CoreViewService() {

    // Components
    private val stateModel: ServiceStateModel by inject()
    private val receiver: ServiceBroadcastReceiver by inject()

    // Views
    private val profileView: ProfileView by inject()

    override fun onCreate() {
        super.onCreate()
        lifecycle.addObserver(stateModel)
        lifecycleScope.launchWhenStarted {
            launch { observeState() }
        }
    }

    private suspend fun observeState() = withContext(Dispatchers.Main) {
        stateModel.state.collect { state ->
            when (state) {
                is ServiceState.Initialized -> onInitialized()
                else -> Unit
            }
        }
    }

    private suspend fun onInitialized() =
        withContext(Dispatchers.Main) {
            startForeground(NOTIFICATION_ID, buildNotification())
            registerReceiver()
            profileView.createAndAttach()
        }

    private suspend fun registerReceiver() = withContext(Dispatchers.IO) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Broadcasts.SERVICE_VIEW_ACTION_STOP)
        registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        profileView.onConfigurationChanged(newConfig)
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, SampleViewService::class.java)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) context.startForegroundService(intent)
            else context.startService(intent)
        }
    }
}
