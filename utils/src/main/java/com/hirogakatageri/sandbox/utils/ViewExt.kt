package com.hirogakatageri.sandbox.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Rect
import android.view.View
import androidx.fragment.app.Fragment
import com.github.ajalt.timberkt.Timber.d

fun <T : View> T.onScreenResize(onResize: T.(isScreenSmaller: Boolean) -> Unit) {

    this.viewTreeObserver.addOnGlobalLayoutListener {

        val r = Rect()
        this.getWindowVisibleDisplayFrame(r)
        val screenHeight = this.rootView.height
        val keypadHeight = screenHeight - r.bottom
        val ratio = 0.15

        if (keypadHeight > screenHeight * ratio)
            onResize(true)
        else
            onResize(false)
    }

}

inline fun <T : View> T.isNotDestroyed(func: () -> Unit) {
    var context = this.context

    while (context is ContextWrapper) {
        if (context is Activity) context.isNotDestroyed { func() }
        else if (context is Fragment) context.isNotDestroyed { func() }
        context = context.baseContext
    }
}

inline fun Context.isNotDestroyed(func: () -> Unit) {
    if (this is Activity && !isFinishing && !isDestroyed) func()
    else if (this is Fragment && !isRemoving && !isDetached) func()
    else d { "Context is destroyed, function is not called." }
}