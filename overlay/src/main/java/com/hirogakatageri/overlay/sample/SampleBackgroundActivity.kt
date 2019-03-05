package com.hirogakatageri.overlay.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hirogakatageri.overlay.R
import kotlinx.android.synthetic.main.activity_background.*

class SampleBackgroundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_background)

        btn_click_me.setOnClickListener {
            Toast.makeText(applicationContext, "Background", Toast.LENGTH_SHORT)
                .show()
        }
        startActivity(Intent(applicationContext, SampleOverlayActivity::class.java))


    }
}