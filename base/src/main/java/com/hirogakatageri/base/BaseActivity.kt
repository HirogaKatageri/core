package com.hirogakatageri.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel

abstract class BaseActivity<out VM : ViewModel> : AppCompatActivity() {

    abstract val titleResId: Int?
    abstract val layoutResId: Int?

    abstract val model: VM

    val toolbar: Toolbar? get() = findViewById(R.id.toolbar)

    abstract fun loadContent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupContent()
        setupTitle()
        loadContent()
    }

    private fun setupContent() = layoutResId?.let { id -> setContentView(id) }
    private fun setupTitle() = titleResId?.let { title -> toolbar?.setTitle(title) }

}