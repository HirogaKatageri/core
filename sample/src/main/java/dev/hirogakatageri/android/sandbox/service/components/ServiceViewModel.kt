package dev.hirogakatageri.android.sandbox.service.components

import com.github.ajalt.timberkt.d
import dev.hirogakatageri.android.sandbox.service.ServiceEvent
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException
import kotlin.coroutines.CoroutineContext

abstract class ServiceViewModel : CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    /**
     * Notifies the ViewModel that an event has been triggered.
     * */
    abstract fun notifyEvent(event: ServiceEvent)

    /**
     * Callback method to notify the ViewModel the associated View has been detached from the
     * WindowManager. By default this method is called during the onDetach of AbstractServiceView.
     * */
    open fun onDetach() = launch {
        d { "${this@ServiceViewModel::class.simpleName} Detached" }
        job.cancelChildren(CancellationException("View is detached"))
    }

}