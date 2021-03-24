package com.hirogakatageri.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.github.ajalt.timberkt.d
import kotlinx.coroutines.*
import org.koin.androidx.scope.ScopeFragment
import kotlin.coroutines.CoroutineContext

@Keep
abstract class CoreFragment<VB : ViewBinding> : ScopeFragment(),
    CoroutineScope,
    LifecycleObserver {

    private lateinit var binding: VB

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    protected abstract fun createBinding(container: ViewGroup?): VB

    /**
     * This is where you would usually bind data to your views.
     * */
    protected abstract fun VB.bind()

    /**
     * Use this to access the views of your fragment.
     * It will always run in the main thread.
     * */
    protected fun binding(func: VB.() -> Unit) = launch { binding.run(func) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewLifecycleOwner.lifecycle.addObserver(this)
        binding = createBinding(container)
        binding.bind()
        return binding.root
    }

    @OnLifecycleEvent(Event.ON_STOP)
    private fun destroyJob() {
        d { "${this.javaClass.simpleName} stopped." }
        coroutineContext.cancelChildren(CancellationException("${this.javaClass.simpleName} stopped."))
    }

}