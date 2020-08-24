package com.hirogakatageri.core.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.full.createType
import kotlin.reflect.full.functions

@Keep
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), CoroutineScope {

    /**
     * To initialize call inflate<ViewBindingClass>()
     * */
    protected abstract val binding: VB

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    /**
     * Called after onCreate() using the main thread.
     * */
    protected abstract suspend fun VB.bind()

    @Suppress("UNCHECKED_CAST")
    protected inline fun <reified T : VB> inflate(): VB = T::class.functions.find { func ->
        func.name == "inflate" && func.parameters[0].type.classifier == LayoutInflater::class.createType().classifier
    }?.call(layoutInflater) as VB

    protected suspend inline fun binding(crossinline func: VB.() -> Unit) = withContext(Dispatchers.Main) { binding.run(func) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        launch { binding.bind() }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancelChildren(CancellationException("${this.javaClass.simpleName} is Destroyed..."))
    }
}