package com.hirogakatageri.core

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.Keep
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.annotation.ExperimentalCoilApi
import coil.request.CachePolicy
import coil.transition.Transition
import com.github.ajalt.timberkt.Timber
import com.hirogakatageri.core.utils.livedata.NetworkLiveData
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalCoilApi
@KoinExperimentalAPI
@Keep
abstract class CoreApplication : Application(), ImageLoaderFactory {

    abstract val moduleList: List<Module>

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(
                if (BuildConfig.DEBUG) Level.DEBUG
                else Level.NONE
            )
            fragmentFactory()
            androidContext(this@CoreApplication)
            modules(coreModule())
            modules(moduleList)
        }
    }

    fun coreModule(): Module = module {
        factory<ConnectivityManager> { getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
        single<NetworkLiveData> { NetworkLiveData(get()) }
    }

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .transition(Transition.NONE)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
}