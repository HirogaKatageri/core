package dev.hirogakatageri.android.sandbox

import android.os.Build
import com.github.ajalt.timberkt.Timber
import com.jakewharton.threetenabp.AndroidThreeTen
import dev.hirogakatageri.android.sandbox.service.ServiceController
import dev.hirogakatageri.android.sandbox.service.ViewService
import dev.hirogakatageri.android.sandbox.service.ViewServiceBroadcastReceiver
import dev.hirogakatageri.android.sandbox.service.components.ServiceViewModelFactory
import dev.hirogakatageri.android.sandbox.service.ui.profile.ProfileView
import dev.hirogakatageri.android.sandbox.service.ui.profile.ProfileViewModel
import dev.hirogakatageri.android.sandbox.service.util.ServiceProvider
import dev.hirogakatageri.android.sandbox.ui.main.MainActivity
import dev.hirogakatageri.android.sandbox.ui.main.MainFragment
import dev.hirogakatageri.android.sandbox.ui.main.MainViewModel
import dev.hirogakatageri.android.sandbox.ui.oauth.OAuthFragment
import dev.hirogakatageri.android.sandbox.ui.oauth.OAuthViewModel
import dev.hirogakatageri.android.sandbox.ui.time.TimeFragment
import dev.hirogakatageri.android.sandbox.ui.time.TimeViewModel
import dev.hirogakatageri.android.sandbox.util.Clock
import dev.hirogakatageri.android.sandbox.util.createNotificationChannels
import dev.hirogakatageri.android.sandbox.util.deleteNotificationChannels
import dev.hirogakatageri.core.CoreApp
import dev.hirogakatageri.oauth2client.OAuthPreferences
import dev.hirogakatageri.oauth2client.TwitchClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinApplication
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.module

private val mainModule = module {

    single { OAuthPreferences(androidContext()) }
    single {
        TwitchClient(
            androidContext(),
            "https://id.twitch.tv/oauth2/authorize",
            "https://id.twitch.tv/oauth2/token",
            BuildConfig.TWITCH_CLIENT_ID,
            "channel:manage:broadcast channel:read:stream_key",
            get(),
            BuildConfig.TWITCH_SECRET_KEY
        )
    }

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
    scope<OAuthFragment> { }
}

private val serviceModule = module {

    factory<ServiceViewModelFactory> {
        ServiceViewModelFactory()
    }

    scope<ViewService> {
        scoped<ServiceProvider> { (service: ViewService) -> ServiceProvider(service) }
        scoped<ServiceController> { ServiceController() }
        scoped<ViewServiceBroadcastReceiver> { ViewServiceBroadcastReceiver() }

        scoped<ProfileViewModel> {
            get<ServiceViewModelFactory>().create(ProfileViewModel::class)
        }
        scoped<ProfileView> { ProfileView(get(), get(), get()) }
    }
}

class App : CoreApp() {

    override val moduleList: List<Module> = listOf(mainModule, serviceModule)

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