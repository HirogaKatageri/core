package dev.hirogakatageri.android.sandbox.service

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.github.ajalt.timberkt.d
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

class ServiceStateManager : LifecycleObserver, CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val _state: MutableStateFlow<ServiceState> = MutableStateFlow(
        ServiceState.Initialized()
    )

    val state: StateFlow<ServiceState> = _state

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onServiceDestroyed() {
        _state.value = ServiceState.Destroyed()
    }

}