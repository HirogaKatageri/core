package com.hirogakatageri.overlay

import android.view.Window
import android.view.WindowManager

class Overlay(
    private val maxWidth: Int,
    private val maxHeight: Int,
    private val width: Int,
    private val height: Int,
    private val position: Position,
    private val window: Window
) {

    var isCollapsed = false

    fun collapse() {

        when (position) {
            Position.TOP -> {
                window.attributes.y = height / 2 - maxHeight / 2
            }
            Position.LEFT -> {
                window.attributes.x = width / 2 - maxWidth / 2
            }
            Position.RIGHT -> {
                window.attributes.x = maxWidth / 2 - width / 2
            }
            Position.BOTTOM -> {
                window.attributes.y = maxHeight / 2 - height / 2
            }
        }

        window.setLayout(width, height)
        window.addFlags(WindowManager.LayoutParams.FLAG_SPLIT_TOUCH)

        isCollapsed = true
    }

    fun uncollapse() {
        window.setLayout(maxWidth, maxHeight)
        window.attributes.x = 0
        window.attributes.y = 0
        isCollapsed = false
    }

}