package com.hirogakatageri.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.fragment.app.Fragment
import com.github.ajalt.timberkt.Timber.d

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