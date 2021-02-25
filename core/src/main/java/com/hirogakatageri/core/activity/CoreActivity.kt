package com.hirogakatageri.core.activity

import android.os.Bundle
import androidx.annotation.Keep
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.github.ajalt.timberkt.d
import kotlinx.coroutines.*
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.scope.ScopeActivity
import org.koin.core.KoinExperimentalAPI
import kotlin.coroutines.CoroutineContext

@KoinExperimentalAPI
@Keep
abstract class CoreActivity<VB : ViewBinding> : ScopeActivity(),
    CoroutineScope,
    LifecycleObserver {

    private lateinit var binding: VB
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    protected abstract fun createBinding(): VB

    /**
     * Called after initializing ViewBinding in "onCreate".
     * */
    protected abstract fun VB.bind()

    /**
     * Function to access ViewBinding object within Activity.
     * */
    protected fun binding(func: VB.() -> Unit) = launch { binding.run(func) }

    @OnLifecycleEvent(Event.ON_CREATE)
    private fun createJob() {
        job = SupervisorJob()
    }

    @OnLifecycleEvent(Event.ON_STOP)
    private fun destroyJob() {
        d { "${this.javaClass.simpleName} paused." }
        coroutineContext.cancelChildren(CancellationException("${this.javaClass.simpleName} paused."))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycle.addObserver(this)
        super.onCreate(savedInstanceState)
        binding = createBinding()
        setContentView(binding.root)
        binding.bind()
    }
}