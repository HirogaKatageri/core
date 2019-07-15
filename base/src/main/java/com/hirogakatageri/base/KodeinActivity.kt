package com.hirogakatageri.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger

abstract class KodeinActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by lazy { (applicationContext as KodeinAware).kodein }

    override val kodeinTrigger = KodeinTrigger()

    abstract val layoutResId: Int

    open val toolbarId: Int = 0

    open val hasBackButton: Boolean = false

    open val titleResId: Int = 0

    val toolbar: Toolbar?
        get() = findViewById(toolbarId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kodeinTrigger.trigger()
        setContentView(layoutResId)

        prepareToolbar()
    }

    private fun prepareToolbar() {

        toolbar?.let {

            if (hasBackButton) {
                setSupportActionBar(it)
                supportActionBar?.run {
                    setDisplayHomeAsUpEnabled(true)
                    setHomeButtonEnabled(true)
                }
            }

            if (titleResId != 0) {
                it.setTitle(titleResId)
            }
        }
    }
}