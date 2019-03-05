package com.hirogakatageri.sandbox.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class BaseViewModel(app: Application) : AndroidViewModel(app), KodeinAware {

    override val kodein: Kodein by kodein()
}