package com.hirogakatageri.core.utils

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
    lifecycleOwner: LifecycleOwner?,
    private val _onClick: (view: View?) -> Unit
) : View.OnClickListener, LifecycleObserver, CoroutineScope {

    private var isRunning: Boolean = false
    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    override fun onClick(view: View?) {
        if (!isRunning) {
            isRunning = true
            d { "${view?.id} click started." }

            launch {
                _onClick(view)
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
        private val lifecycleOwner: LifecycleOwner? = null,
        private val views: List<View?>? = null,
        private val view: View? = null,
        private val onClick: (view: View?) -> Unit
    ) {
        fun build() {
            val listener = ThrottleClickListener(lifecycleOwner, onClick)
            view?.setOnClickListener(listener)
            views?.forEach { it?.setOnClickListener(listener) }
        }
    }
}