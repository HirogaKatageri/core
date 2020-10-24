package com.hirogakatageri.core.activity

import android.os.Bundle
import androidx.annotation.Keep
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.*
import org.koin.androidx.scope.ScopeActivity
import kotlin.coroutines.CoroutineContext

@Keep
abstract class CoreActivity<VB : ViewBinding> : ScopeActivity(), CoroutineScope {

    lateinit var binding: VB

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    protected abstract fun createBinding(): VB

    /**
     * Called within onCreate() using the main thread.
     * */
    protected abstract suspend fun VB.bind()

    protected inline fun binding(crossinline func: VB.() -> Unit) = launch { binding.run(func) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createBinding()
        setContentView(binding.root)
        launch { binding.bind() }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancelChildren(CancellationException("${this.javaClass.simpleName} is Destroyed..."))
    }
}