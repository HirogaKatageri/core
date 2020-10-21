package com.hirogakatageri.core.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.*
import org.koin.androidx.scope.ScopeActivity
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.full.createType
import kotlin.reflect.full.functions

@Keep
abstract class BaseActivity<VB : ViewBinding> : ScopeActivity(), CoroutineScope {

    lateinit var binding: VB

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    /**
     * Called within onCreate() using the main thread.
     * */
    protected abstract suspend fun VB.bind()

    protected abstract fun createBinding(): VB

    protected suspend inline fun binding(crossinline func: VB.() -> Unit) = withContext(Dispatchers.Main) { binding.run(func) }

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