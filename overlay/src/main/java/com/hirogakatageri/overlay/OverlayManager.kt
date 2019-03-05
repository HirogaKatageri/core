package com.hirogakatageri.overlay

import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity

class OverlayManager(private val activity: AppCompatActivity) {

    private val displayMetrics = DisplayMetrics()
    private val windowManager by lazy { activity.windowManager }
    private val window by lazy { activity.window }

    private val maxHeight by lazy { displayMetrics.heightPixels }
    private val maxWidth by lazy { displayMetrics.widthPixels }

    init {
        windowManager.defaultDisplay.getMetrics(displayMetrics)
    }

    private fun Float.toDp() =
        this / (activity.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

    private fun Float.toPixels() =
        this * (activity.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

    private var _position = Position.BOTTOM
    private var _heightDp = 0f
    private var _widthDp = 0f

    fun withCollapsedHeight(heightDp: Float): OverlayManager {
        _heightDp = heightDp
        return this
    }

    fun withCollapsedWidth(widthDp: Float): OverlayManager {
        _widthDp = widthDp
        return this
    }

    fun withWidthMax(): OverlayManager {
        _widthDp = maxWidth.toFloat()
        return this
    }

    fun withHeightMax(): OverlayManager {
        _heightDp = maxHeight.toFloat()
        return this
    }

    fun withPosition(position: Position): OverlayManager {
        _position = position
        return this
    }

    fun build() = Overlay(
        maxWidth,
        maxHeight,
        _widthDp.toPixels().toInt(),
        _heightDp.toPixels().toInt(),
        _position,
        window
    )

}