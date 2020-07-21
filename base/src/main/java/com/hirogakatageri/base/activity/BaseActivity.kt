package com.hirogakatageri.base.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.full.createType
import kotlin.reflect.full.functions

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), CoroutineScope {

    abstract val binding: VB

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job = SupervisorJob()

    abstract suspend fun VB.bind()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : VB> inflate(): VB = T::class.functions.find { func ->
        func.name == "inflate" && func.parameters[0].type.classifier == LayoutInflater::class.createType().classifier
    }?.call(layoutInflater) as VB

    inline fun views(crossinline func: VB.() -> Unit) = launch { binding.run(func) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        launch { binding.bind() }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancelChildren()
    }
}