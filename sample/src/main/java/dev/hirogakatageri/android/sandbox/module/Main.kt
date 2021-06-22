package dev.hirogakatageri.android.sandbox.module

import dev.hirogakatageri.android.sandbox.BuildConfig
import dev.hirogakatageri.android.sandbox.ui.main.MainActivity
import dev.hirogakatageri.android.sandbox.ui.main.MainFragment
import dev.hirogakatageri.android.sandbox.ui.main.MainViewModel
import dev.hirogakatageri.android.sandbox.ui.oauth.OAuthFragment
import dev.hirogakatageri.android.sandbox.ui.oauth.OAuthViewModel
import dev.hirogakatageri.android.sandbox.ui.time.TimeFragment
import dev.hirogakatageri.android.sandbox.ui.time.TimeViewModel
import dev.hirogakatageri.android.sandbox.util.Clock
import dev.hirogakatageri.oauth2client.util.OAuthPreferences
import dev.hirogakatageri.oauth2client.twitch.TwitchClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val mainModule = module {

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