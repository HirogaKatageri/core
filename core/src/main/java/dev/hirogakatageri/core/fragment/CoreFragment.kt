package dev.hirogakatageri.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import org.koin.androidx.scope.ScopeFragment

@Keep
abstract class CoreFragment<VB : ViewBinding> : ScopeFragment() {

    protected var binding: VB? = null

    protected val lifecycleScope get() = viewLifecycleOwner.lifecycleScope

    /**
     * Function to initialize ViewBinding.
     * @return VB the type of ViewBinding used by the Fragment.
     * */
    protected abstract fun createBinding(container: ViewGroup?): VB

    /**
     * Called after initializing ViewBinding in "onCreateView".
     * */
    protected abstract fun VB.bind()

    /**
     * Function to easily access ViewBinding in the Fragment.
     * It will always run in the main thread.
     * It will only run when binding is not null.
     * */
    protected fun binding(func: VB.() -> Unit) =
        lifecycleScope.launchWhenStarted { binding?.run(func) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = createBinding(container)
        binding?.bind()
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}