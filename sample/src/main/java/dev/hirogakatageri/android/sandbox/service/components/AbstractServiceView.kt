package dev.hirogakatageri.android.sandbox.service.components

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.github.ajalt.timberkt.d
import dev.hirogakatageri.android.sandbox.R
import dev.hirogakatageri.android.sandbox.service.ServiceState
import dev.hirogakatageri.android.sandbox.service.ServiceStateManager
import dev.hirogakatageri.android.sandbox.service.util.ViewServiceProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

abstract class AbstractServiceView<VB : ViewBinding, out VM : ServiceViewModel>(
    protected val serviceProvider: ViewServiceProvider,
    protected val serviceState: ServiceStateManager,
    protected val viewModelFactory: ServiceViewModelFactory
) : CoroutineScope, LifecycleObserver {

    /**
     * Used to determine what ViewModel will be instantiated by ServiceViewModelFactory.
     * */
    abstract val viewType: ServiceViewType

    /**
     * The View Model of the View. Instantiated by the ViewModelFactory using ServiceViewType.
     * @see ServiceViewType
     * @see ServiceViewModelFactory
     * */
    protected val vm: VM by lazy { viewModelFactory.create(viewType) as VM }

    // Coroutines Setup
    protected val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    @StyleRes
    var themeResId: Int = R.style.Theme_Core

    /**
     * State of attachment to WindowManager. It is only changed in the attach() and detach().
     * */
    var isAttached: Boolean = false

    init {
        // Retrieves reference to service and attaches this as a Lifecycle Observer.
        // To detach the view when Service is destroyed.
        serviceProvider.getService().lifecycle.addObserver(this)
    }

    protected lateinit var layoutParams: WindowManager.LayoutParams
    protected lateinit var binding: VB

    protected val windowManager: WindowManager?
        get() = serviceProvider
            .getService()
            .getSystemService(Context.WINDOW_SERVICE) as? WindowManager

    protected val layoutInflater: LayoutInflater
        get() = ContextThemeWrapper(
            serviceProvider.getService(),
            themeResId
        ).let { wrapper -> LayoutInflater.from(wrapper) }

    protected val windowRect: Rect?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            windowManager?.currentWindowMetrics?.bounds
        else Point().let {
            windowManager?.defaultDisplay?.getSize(it)
            Rect(0, 0, it.x, it.y)
        }

    protected val rotation: Int?
        get() = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
            serviceProvider.getService().display?.rotation
        else
            windowManager?.defaultDisplay?.rotation

    open fun bindView() {
        launch {
            serviceState.state.collect { state ->
                if (state is ServiceState.Destroyed)
                    job.cancelChildren(CancellationException("Service:DESTROYED"))
            }
        }
    }


    fun attach() = launch {
        if (!isAttached) {
            isAttached = true
            windowManager?.addView(binding.root, layoutParams)
            onAttach()
        }
    }

    fun bindAndAttach() = launch {
        bindView()
        attach()
    }

    open suspend fun onAttach() = withContext(Dispatchers.Main) {
        d { "${this::class.simpleName} Attached" }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detach() {
        job.cancelChildren()
        launch {
            if (isAttached) {
                isAttached = false
                windowManager?.removeView(binding.root)
                onDetach()
            }
        }
    }

    open suspend fun onDetach() = withContext(Dispatchers.Main) {
        d { "${this::class.simpleName} Detached" }
        vm.onDetach()
    }

}