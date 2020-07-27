package com.hirogakatageri.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.full.createType
import kotlin.reflect.full.functions

@Keep
abstract class BaseFragment<VB : ViewBinding> : Fragment(), CoroutineScope {

    var binding: VB? = null

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    abstract fun createBinding(container: ViewGroup?): VB

    abstract suspend fun VB.bind()

    abstract suspend fun afterBind()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : VB> inflate(viewGroup: ViewGroup?): VB =
        T::class.functions.find { func ->
            func.name == "inflate" &&
                    func.parameters.size == 3 &&
                    func.parameters[0].type.classifier == LayoutInflater::class.createType().classifier &&
                    func.parameters[1].type.classifier == ViewGroup::class.createType().classifier &&
                    func.parameters[2].type.classifier == Boolean::class.createType().classifier
        }?.call(layoutInflater, viewGroup, false) as VB

    inline fun views(crossinline func: VB.() -> Unit) = launch { binding?.run(func) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = createBinding(container)
        launch {
            binding?.bind()
            afterBind()
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}