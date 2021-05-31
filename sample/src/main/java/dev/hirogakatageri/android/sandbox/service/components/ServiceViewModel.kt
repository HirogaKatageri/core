package dev.hirogakatageri.android.sandbox.service.components

import com.github.ajalt.timberkt.d
import dev.hirogakatageri.android.sandbox.service.ServiceEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import java.util.concurrent.CancellationException
import kotlin.coroutines.CoroutineContext

abstract class ServiceViewModel : CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    abstract fun notifyEvent(event: ServiceEvent)

    fun onDetach() {
        d { "${this::class.simpleName} Detached" }
        job.cancelChildren(CancellationException("ViewModel:${this::class.simpleName}:DESTROYED"))
    }

}