package com.hirogakatageri.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.full.createType
import kotlin.reflect.full.functions

@Keep
abstract class BaseFragment<VB : ViewBinding> : Fragment(), CoroutineScope {

    protected lateinit var binding: VB

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    /**
     * Called within onCreateView using the main thread.
     * */
    protected abstract suspend fun VB.bind()

    /**
     * Called after bind() using the main thread.
     * */
    protected abstract suspend fun VB.afterBind()

    /**
     * To create ViewBinding call inflate<ViewBindingClass>(container)
     * */
    protected abstract fun createBinding(container: ViewGroup?): VB

    @Suppress("UNCHECKED_CAST")
    protected inline fun <reified T : VB> inflate(viewGroup: ViewGroup?): VB =
        T::class.functions.find { func ->
            func.name == "inflate" &&
                    func.parameters.size == 3 &&
                    func.parameters[0].type.classifier == LayoutInflater::class.createType().classifier &&
                    func.parameters[1].type.classifier == ViewGroup::class.createType().classifier &&
                    func.parameters[2].type.classifier == Boolean::class.createType().classifier
        }?.call(layoutInflater, viewGroup, false) as VB

    protected suspend inline fun binding(crossinline func: VB.() -> Unit) = withContext(Dispatchers.Main) { binding.run(func) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = createBinding(container)
        launch {
            binding.bind()
            binding.afterBind()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineContext.cancelChildren(CancellationException("${this.javaClass.simpleName} is Destroyed..."))
    }
}