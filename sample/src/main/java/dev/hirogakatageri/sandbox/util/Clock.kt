package dev.hirogakatageri.sandbox.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.github.ajalt.timberkt.Timber.d
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import kotlin.coroutines.CoroutineContext

class Clock : LifecycleObserver, CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext = Dispatchers.IO + job

    private var ticker: ReceiveChannel<Unit>? = null
    val time: String get() = LocalDateTime.now().toString()

    private var _onTick: (time: String) -> Unit = {}

    fun attachLifecycle(
        lifecycleOwner: LifecycleOwner,
        onTick: (time: String) -> Unit = {}
    ) {
        lifecycleOwner.lifecycle.addObserver(this)
        _onTick = onTick
    }

    @ObsoleteCoroutinesApi
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        d { "Starting clock..." }
        ticker = ticker(
            delayMillis = 1000,
            initialDelayMillis = 0,
            context = Dispatchers.IO
        )

        launch {
            ticker?.consumeAsFlow()?.collect {
                d { "Clock is ticking..." }
                _onTick(time)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        job.cancelChildren()
        ticker?.cancel()
        d { "Clock stopped..." }
    }
}
