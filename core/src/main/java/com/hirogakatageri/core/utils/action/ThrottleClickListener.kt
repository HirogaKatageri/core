package com.hirogakatageri.core.utils.action

import android.view.View
import androidx.annotation.Keep
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.github.ajalt.timberkt.Timber.d
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@Keep
class ThrottleClickListener(
    lifecyclerOwner: LifecycleOwner?,
    private val click: (view: View?) -> Unit
) : View.OnClickListener, LifecycleObserver, CoroutineScope {

    private var isRunning: Boolean = false
    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    init {
        lifecyclerOwner?.lifecycle?.addObserver(this)
    }

    override fun onClick(view: View?) {
        if (!isRunning) {
            isRunning = true
            d { "${view?.id} click started." }

            launch {
                click(view)
                delay(800)
                isRunning = false
                d { "${view?.id} click finished." }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        job.cancelChildren()
        isRunning = false
    }

    @Keep
    class Builder(
        val lifecycleOwner: LifecycleOwner? = null,
        val views: List<View?>? = null,
        val view: View? = null,
        val onClick: (view: View?) -> Unit
    ) {
        fun build() {
            val listener = ThrottleClickListener(lifecycleOwner, onClick)
            view?.setOnClickListener(listener)
            views?.forEach { it?.setOnClickListener(listener) }
        }
    }
}