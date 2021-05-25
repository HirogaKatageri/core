package dev.hirogakatageri.core

import android.app.Application
import androidx.annotation.Keep
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

@Keep
abstract class CoreApp : Application() {

    protected abstract val moduleList: List<Module>

    override fun onCreate() {
        super.onCreate()

        startKoin {
            onStartKoin()
            androidContext(this@CoreApp)
            fragmentFactory()
            modules(moduleList)
        }
    }

    protected open fun KoinApplication.onStartKoin() {}
}