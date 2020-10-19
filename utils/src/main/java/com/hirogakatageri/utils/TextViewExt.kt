package com.hirogakatageri.utils

import android.text.Spannable
import android.widget.TextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import kotlinx.coroutines.*

fun TextView.setTextSafely(spannable: Spannable?) = CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
    isNotDestroyed {
        val preComputedText = PrecomputedTextCompat.create(
            spannable ?: "",
            TextViewCompat.getTextMetricsParams(this@setTextSafely)
        )

        withContext(Dispatchers.Main) {
            TextViewCompat.setPrecomputedText(this@setTextSafely, preComputedText)
        }
    }
}

fun TextView.setTextSafely(string: String?) = CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
    isNotDestroyed {
        val preComputedText = PrecomputedTextCompat.create(
            string ?: "",
            TextViewCompat.getTextMetricsParams(this@setTextSafely)
        )

        withContext(Dispatchers.Main) {
            TextViewCompat.setPrecomputedText(this@setTextSafely, preComputedText)
        }
    }
}