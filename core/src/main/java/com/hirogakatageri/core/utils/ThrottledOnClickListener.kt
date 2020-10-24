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

/**
 * OnClickListener that does not allow
 * */
@Keep
class ThrottledOnClickListener private constructor(
    lifecycleOwner: LifecycleOwner?,
    private val delayMs: Long,
    private val _onClick: (view: View?) -> Unit
) : View.OnClickListener,
    LifecycleObserver,
    CoroutineScope {

    private var isRunning: Boolean = false
    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    override fun onClick(view: View?) {
        if (!isRunning) {
            isRunning = true

            launch {
                d { "Click start..." }
                _onClick(view)
                d { "Click finished..." }

                d { "Delay start..." }
                delay(delayMs)
                d { "Delay finished..." }
                isRunning = false
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
        private val delayMs: Long = 1000,
        private val views: List<View?>? = null,
        private val view: View? = null,
        private val onClick: (view: View?) -> Unit
    ) {
        fun build(): ThrottledOnClickListener {
            val listener = ThrottledOnClickListener(
                lifecycleOwner = lifecycleOwner,
                delayMs = delayMs,
                _onClick = onClick
            )
            view?.setOnClickListener(listener)
            views?.forEach { it?.setOnClickListener(listener) }
            return listener
        }
    }

    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }
}