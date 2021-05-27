package dev.hirogakatageri.android.sandbox

import com.github.ajalt.timberkt.Timber
import com.jakewharton.threetenabp.AndroidThreeTen
import dev.hirogakatageri.android.sandbox.api.OAuthPreferences
import dev.hirogakatageri.android.sandbox.api.TwitchClient
import dev.hirogakatageri.android.sandbox.ui.main.MainActivity
import dev.hirogakatageri.android.sandbox.ui.main.MainFragment
import dev.hirogakatageri.android.sandbox.ui.main.MainViewModel
import dev.hirogakatageri.android.sandbox.ui.oauth.OAuthFragment
import dev.hirogakatageri.android.sandbox.ui.oauth.OAuthViewModel
import dev.hirogakatageri.android.sandbox.ui.time.TimeFragment
import dev.hirogakatageri.android.sandbox.ui.time.TimeViewModel
import dev.hirogakatageri.android.sandbox.util.Clock
import dev.hirogakatageri.core.BuildConfig
import dev.hirogakatageri.core.CoreApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinApplication
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.module
import java.lang.ref.WeakReference

private val mainModule = module {

    single { OAuthPreferences(WeakReference(androidContext())) }
    single { TwitchClient(androidContext(), get()) }
    factory { Clock() }

    viewModel { MainViewModel() }
    viewModel { TimeViewModel(get()) }
    viewModel { OAuthViewModel(get()) }

    scope<MainActivity> {
        fragment { MainFragment() }
        fragment { TimeFragment() }
        fragment { OAuthFragment() }
    }

    scope<MainFragment> { }
    scope<TimeFragment> { }
    scope<OAuthFragment> {}
}

class App : CoreApp() {

    override val moduleList: List<Module> = listOf(mainModule)

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    override fun KoinApplication.onStartKoin() {
        androidLogger(
            if (BuildConfig.DEBUG) Level.DEBUG
            else Level.NONE
        )
    }
}