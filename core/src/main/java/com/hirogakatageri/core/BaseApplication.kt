package com.hirogakatageri.core

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.Keep
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import com.github.ajalt.timberkt.Timber
import com.hirogakatageri.core.utils.NetworkLiveData
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.module

@Keep
abstract class BaseApplication : Application(), ImageLoaderFactory {

    abstract val moduleList: List<Module>

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(
                if (BuildConfig.DEBUG) Level.ERROR
                else Level.NONE
            )
            fragmentFactory()
            androidContext(this@BaseApplication)
            modules(moduleList)
        }
    }

    fun coreModule() = module {
        factory<ConnectivityManager> { getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
        single { NetworkLiveData(get()) }
    }

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .crossfade(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.DISABLED)
        .build()
}