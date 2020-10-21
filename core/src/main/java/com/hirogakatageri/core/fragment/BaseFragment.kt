package com.hirogakatageri.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.*
import org.koin.androidx.scope.ScopeFragment
import kotlin.coroutines.CoroutineContext

@Keep
abstract class BaseFragment<VB : ViewBinding> : ScopeFragment(), CoroutineScope {

    lateinit var binding: VB

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

    protected abstract fun createBinding(container: ViewGroup?): VB

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