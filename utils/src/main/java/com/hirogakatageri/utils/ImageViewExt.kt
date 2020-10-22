package com.hirogakatageri.utils

import android.graphics.ColorMatrixColorFilter
import android.widget.ImageView

/**
 * Inverts color of Image.
 * @see <a href="https://stackoverflow.com/questions/17841787/invert-colors-of-drawable">Invert colors of drawable</a>
 * */
fun ImageView.invertColors() {
    colorFilter = ColorMatrixColorFilter(
        floatArrayOf(
            -1.0f, .0f, .0f, .0f, 255.0f,
            .0f, -1.0f, .0f, .0f, 255.0f,
            .0f, .0f, -1.0f, .0f, 255.0f,
            .0f, .0f, .0f, 1.0f, .0f
        )
    )
}