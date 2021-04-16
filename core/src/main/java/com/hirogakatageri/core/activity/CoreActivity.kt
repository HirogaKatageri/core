package com.hirogakatageri.core.activity

import android.os.Bundle
import androidx.annotation.Keep
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.github.ajalt.timberkt.d
import kotlinx.coroutines.*
import org.koin.androidx.scope.ScopeActivity
import kotlin.coroutines.CoroutineContext

@Keep
abstract class CoreActivity<VB : ViewBinding> : ScopeActivity(),
    CoroutineScope,
    LifecycleObserver {

    protected lateinit var binding: VB

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    /**
     * Function to initialize ViewBinding.
     * @return VB the type of ViewBinding used by the Activity.
     * */
    protected abstract fun createBinding(): VB

    /**
     * Called after initializing ViewBinding in "onCreate".
     * */
    protected abstract fun VB.bind()

    /**
     * Function to easily access ViewBinding in the Activity.
     * It will always run in the main thread.
     * */
    protected fun binding(func: VB.() -> Unit): Job = launch { binding.run(func) }

    @OnLifecycleEvent(Event.ON_STOP)
    protected open fun cancelCoroutineJobs() {
        d { "${this::class.simpleName} stopped." }
        job.cancelChildren(CancellationException("${this::class.simpleName} stopped."))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycle.addObserver(this)
        super.onCreate(savedInstanceState)
        binding = createBinding()
        setContentView(binding.root)
        binding.bind()
    }
}