package com.hirogakatageri.base

import android.app.Application
import com.github.ajalt.timberkt.Timber
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

abstract class BaseApplication : Application() {

    abstract val moduleList: List<Module>

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(
                if (BuildConfig.DEBUG) Level.DEBUG
                else Level.NONE
            )
            androidContext(this@BaseApplication)
            modules(moduleList)
        }
    }
}