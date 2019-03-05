package com.hirogakatageri.overlay.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hirogakatageri.overlay.Overlay
import com.hirogakatageri.overlay.OverlayManager
import com.hirogakatageri.overlay.Position
import com.hirogakatageri.overlay.R
import kotlinx.android.synthetic.main.activity_overlay.*

class SampleOverlayActivity : AppCompatActivity() {
    lateinit var overlay: Overlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overlay)

        overlay = OverlayManager(this)
            .withWidthMax()
            .withCollapsedHeight(128f)
            .withPosition(Position.BOTTOM)
            .build()

        overlay.collapse()

        btn_click_me.setOnClickListener {
            Toast.makeText(applicationContext, "Overlay", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onBackPressed() {
        if (!overlay.isCollapsed) overlay.collapse() else overlay.uncollapse()
    }
}