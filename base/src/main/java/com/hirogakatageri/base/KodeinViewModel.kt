package com.hirogakatageri.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger

abstract class KodeinViewModel(app: Application) : AndroidViewModel(app), KodeinAware {

    override val kodein: Kodein by lazy { (app as KodeinAware).kodein }

    final override val kodeinTrigger = KodeinTrigger()

    init {
        kodeinTrigger.trigger()
    }

}