package dev.hirogakatageri.sandbox.service

import androidx.lifecycle.LifecycleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

class ServiceStateModel : LifecycleObserver, CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val _state: MutableStateFlow<ServiceState> = MutableStateFlow(
        ServiceState.Initialized()
    )

    val state: StateFlow<ServiceState> = _state
}
