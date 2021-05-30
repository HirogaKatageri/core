package dev.hirogakatageri.android.sandbox.service

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Build
import androidx.lifecycle.lifecycleScope
import dev.hirogakatageri.android.sandbox.service.ui.profile.ProfileView
import dev.hirogakatageri.android.sandbox.util.Broadcasts
import dev.hirogakatageri.android.sandbox.util.buildNotification
import dev.hirogakatageri.core.service.CoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val NOTIFICATION_ID: Int = 0x2a93a245

class ViewService : CoreService() {

    // Components
    private val stateManager: ServiceStateManager by inject()
    private val broadcastReceiver: ViewServiceBroadcastReceiver by inject()

    // Views
    private val profileView: ProfileView by inject()

    override fun onCreate() {
        super.onCreate()
        lifecycle.addObserver(stateManager)
        lifecycleScope.launchWhenStarted {
            launch { observeState() }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    private suspend fun observeState() = withContext(Dispatchers.Main) {
        stateManager.state.collect { state ->
            when (state) {
                is ServiceState.Initialized -> onInitialized()
                is ServiceState.Destroyed -> Unit
                else -> Unit
            }
        }
    }

    private suspend fun onInitialized() = withContext(Dispatchers.Main) {
        startForeground(NOTIFICATION_ID, buildNotification())
        registerReceiver()
        profileView.bindAndAttach()
    }

    private suspend fun registerReceiver() = withContext(Dispatchers.IO) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Broadcasts.SERVICE_VIEW_ACTION_STOP)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        profileView.onConfigurationChanged(newConfig)
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ViewService::class.java)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) context.startForegroundService(intent)
            else context.startService(intent)
        }
    }

}