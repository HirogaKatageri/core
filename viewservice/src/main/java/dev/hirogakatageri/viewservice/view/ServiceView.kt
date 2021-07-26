/*
 *    Copyright 2021 Gian Patrick Quintana
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package dev.hirogakatageri.viewservice.view

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.annotation.Keep
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import dev.hirogakatageri.viewservice.util.LifecycleServiceProvider
import dev.hirogakatageri.viewservice.util.addLifecycleObserver
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * @param serviceProvider ServiceProvider to observe the Lifecycle of the Service.
 * @param themeResId The theme to use when inflating the Layout.
 * */
@Keep
abstract class ServiceView(
    val serviceProvider: LifecycleServiceProvider,
    val themeResId: Int
) : CoroutineScope, LifecycleObserver {

    protected val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private var _isObservingLifecycle: Boolean = false

    private var _isAttached: Boolean = false

    /**
     * @return true when attach() has been called and detach() has not yet been called.
     * */
    val isAttached: Boolean get() = _isAttached

    protected lateinit var layoutParams: WindowManager.LayoutParams
    protected val windowManager: WindowManager
        get() = serviceProvider.service.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    protected val layoutInflater: LayoutInflater
        get() = ContextThemeWrapper(serviceProvider.service, themeResId)
            .let { wrapper -> LayoutInflater.from(wrapper) }

    protected val windowRect: Rect
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            windowManager.currentWindowMetrics.bounds
        else Point().let { point ->
            windowManager.defaultDisplay?.getSize(point)
            Rect(0, 0, point.x, point.y)
        }

    protected val rotation: Int
        get() = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
            serviceProvider.service.display?.rotation ?: 0
        else windowManager.defaultDisplay.rotation

    /**
     * The view of this ServiceView.
     * */
    abstract val view: View

    /**
     * By default this is where you initialize your view.
     * */
    abstract fun createView()

    /**
     * Adds the View to the WindowManager.
     * */
    fun attach() = launch {
        if (!isAttached) {
            _isAttached = true

            if (!_isObservingLifecycle) {
                _isObservingLifecycle = true
                serviceProvider.addLifecycleObserver(this@ServiceView)
            }

            try {
                windowManager.addView(view, layoutParams)
                onAttach()
            } catch (ex: WindowManager.BadTokenException) {
                _isAttached = false
                Log.e(
                    this@ServiceView::class.simpleName,
                    "Unable to add view to WindowManager",
                    ex
                )
            } catch (ex: WindowManager.InvalidDisplayException) {
                _isAttached = false
                Log.e(
                    this@ServiceView::class.simpleName,
                    "Unable to add view to WindowManager",
                    ex
                )
            }
        }
    }

    /**
     * Calls createView() -> attach().
     * @see createView
     * @see attach
     * */
    fun createAndAttach() = launch(Dispatchers.Main) {
        createView()
        attach()
    }

    /**
     * Callback method that notifies this View is now attached to the WindowManager.
     * */
    @CallSuper
    protected open suspend fun onAttach(): Unit = withContext(Dispatchers.Main) {
        Log.i(
            this@ServiceView::class.simpleName,
            "Attached"
        )
    }

    /**
     * Removes the view from the WindowManager. It will cancel all current jobs of the View
     * before attempting to remove the View from the WindowManager. This is called automatically
     * if the view is observing the lifecycle of the Service.
     * */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detach() {
        job.cancelChildren(CancellationException("Service:DESTROYED"))
        launch {
            if (isAttached) {
                _isAttached = false

                windowManager.removeView(view)
                onDetach()
            }
        }
    }

    /**
     * Callback method that notifies this view is now detached from the WindowManager.
     * */
    @CallSuper
    protected open suspend fun onDetach(): Unit = withContext(Dispatchers.Main) {
        Log.i(
            this@ServiceView::class.simpleName,
            "Detached"
        )
    }

    /**
     * Callback method for ServiceView when onConfigurationChanged is called in the Service.
     * Useful for handling screen rotations.
     * */
    open fun onConfigurationChanged(newConfig: Configuration) {}
}
