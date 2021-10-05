package dev.hirogakatageri.sandbox

import android.os.Build
import com.github.ajalt.timberkt.Timber
import com.jakewharton.threetenabp.AndroidThreeTen
import dev.hirogakatageri.core.CoreApp
import dev.hirogakatageri.sandbox.util.createNotificationChannels
import dev.hirogakatageri.sandbox.util.deleteNotificationChannels
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.logger.Level
import org.koin.core.module.Module

class App : CoreApp() {

    override val moduleList: List<Module> = listOf(
        securityModule,
        mainModule,
        viewServiceModule,
        apiModule,
        chatModule
    )

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            deleteNotificationChannels()
            createNotificationChannels()
        }
    }

    override fun KoinApplication.onStartKoin() {
        androidLogger(
            if (BuildConfig.DEBUG) Level.DEBUG
            else Level.NONE
        )
    }
}
