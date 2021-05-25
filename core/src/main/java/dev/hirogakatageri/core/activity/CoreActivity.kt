package dev.hirogakatageri.core.activity

import android.os.Bundle
import androidx.annotation.Keep
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job
import org.koin.androidx.scope.ScopeActivity

@Keep
abstract class CoreActivity<VB : ViewBinding> : ScopeActivity() {

    protected lateinit var binding: VB

    /**
     * Function to initialize ViewBinding.
     * @return VB the type of ViewBinding used by the Activity.
     * */
    protected abstract fun createBinding(): VB

    /**
     * Called after initializing ViewBinding in "onCreate".
     * */
    protected abstract fun VB.bind()

    /**
     * Function to easily access ViewBinding in the Activity.
     * It will always run in the main thread.
     * */
    protected fun binding(func: VB.() -> Unit): Job =
        lifecycleScope.launchWhenStarted { binding.run(func) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createBinding()
        setContentView(binding.root)
        binding.bind()
    }
}