package dev.hirogakatageri.android.sandbox.service.components

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import dev.hirogakatageri.android.sandbox.R
import dev.hirogakatageri.android.sandbox.service.ServiceController
import dev.hirogakatageri.android.sandbox.service.util.ServiceProvider
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class AbstractServiceView<VB : ViewBinding, out VM : ServiceViewModel>(
    protected val serviceProvider: ServiceProvider,
    protected val serviceController: ServiceController,
    protected val vm: VM
) : CoroutineScope, LifecycleObserver {

    protected val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    /**
     * Used to override the theme used in the LayoutInflater.
     * */
    @StyleRes
    open val themeResId: Int = R.style.Theme_Core

    private var _isAttached: Boolean = false

    /**
     * @return true when attach() has been called and detach() has not yet been called.
     * */
    val isAttached: Boolean get() = _isAttached

    init {
        // Attaches View as a LifecycleObserver to Service.
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

    /**
     * By default this is where you initialize your view.
     * */
    abstract fun bindView()

    /**
     * Adds the View to the WindowManager.
     * */
    fun attach() = launch {
        if (!isAttached) {
            _isAttached = true

            val window = windowManager

            if (window != null) {
                try {
                    window.addView(binding.root, layoutParams)
                    onAttach()
                } catch (ex: WindowManager.BadTokenException) {
                    e(ex) { "Unable to add ${this@AbstractServiceView::class.simpleName} to WindowManager" }
                    _isAttached = false
                } catch (ex: WindowManager.InvalidDisplayException) {
                    e(ex) { "Unable to add ${this@AbstractServiceView::class.simpleName} to WindowManager" }
                    _isAttached = false
                }
            } else _isAttached = false
        }
    }

    /**
     * Short function that calls bindView() and attach().
     * */
    fun bindAndAttach() = launch {
        bindView()
        attach()
    }

    /**
     * Callback method that notifies this View is now attached to the WindowManager.
     * */
    open suspend fun onAttach() = withContext(Dispatchers.Main) {
        d { "${this@AbstractServiceView::class.simpleName} Attached" }
    }

    /**
     * Removes the view to the WindowManager. It will cancel all current jobs of the View
     * before attempting to remove the View from the WindowManager. This is called automatically
     * if the view is observing the lifecycle of the Service.
     * */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detach() {
        job.cancelChildren(CancellationException("Service:DESTROYED"))
        launch {
            if (isAttached) {
                _isAttached = false

                val window = windowManager

                if (window != null) {
                    window.removeView(binding.root)
                    onDetach()
                } else e { "Unable to detach ${this@AbstractServiceView::class.simpleName} because WindowManager is null." }
            }
        }
    }

    /**
     * Callback method that notifies this view is now detached from the WindowManager.
     * */
    @CallSuper
    open suspend fun onDetach() = withContext(Dispatchers.Main) {
        d { "${this@AbstractServiceView::class.simpleName} Detached" }
        vm.onDetach()
    }

    /**
     * Callback method for this view when onConfigurationChanged is called in the Service.
     * Useful for handling screen rotations.
     * */
    open fun onConfigurationChanged(newConfig: Configuration) {}
}