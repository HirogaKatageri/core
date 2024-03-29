package dev.hirogakatageri.sandbox

import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.SavedStateHandle
import dev.hirogakatageri.oauth2client.twitch.TwitchClient
import dev.hirogakatageri.oauth2client.util.OAuthPreferences
import dev.hirogakatageri.sandbox.data.ApiClient
import dev.hirogakatageri.sandbox.data.FirebaseManager
import dev.hirogakatageri.sandbox.data.chat.FireChatRepository
import dev.hirogakatageri.sandbox.data.repository.ApiRepository
import dev.hirogakatageri.sandbox.service.SampleViewService
import dev.hirogakatageri.sandbox.service.ServiceStateModel
import dev.hirogakatageri.sandbox.service.ui.ServiceViews
import dev.hirogakatageri.sandbox.service.ui.profile.ProfileView
import dev.hirogakatageri.sandbox.service.util.ServiceBroadcastReceiver
import dev.hirogakatageri.sandbox.service.util.ServiceViewFactory
import dev.hirogakatageri.sandbox.service.util.ServiceViewModelFactory
import dev.hirogakatageri.sandbox.ui.chat.FireChatFragment
import dev.hirogakatageri.sandbox.ui.chat.FireChatMsgController
import dev.hirogakatageri.sandbox.ui.chat.FireChatViewModel
import dev.hirogakatageri.sandbox.ui.fcm.FcmFragment
import dev.hirogakatageri.sandbox.ui.fcm.FcmViewModel
import dev.hirogakatageri.sandbox.ui.feature.FeatureAdapter
import dev.hirogakatageri.sandbox.ui.feature.FeatureFragment
import dev.hirogakatageri.sandbox.ui.feature.FeatureManager
import dev.hirogakatageri.sandbox.ui.feature.FeatureViewModel
import dev.hirogakatageri.sandbox.ui.main.MainActivity
import dev.hirogakatageri.sandbox.ui.main.ParentViewModel
import dev.hirogakatageri.sandbox.ui.oauth.OAuthFragment
import dev.hirogakatageri.sandbox.ui.oauth.OAuthViewModel
import dev.hirogakatageri.sandbox.ui.time.TimeFragment
import dev.hirogakatageri.sandbox.ui.time.TimeViewModel
import dev.hirogakatageri.sandbox.util.Clock
import dev.hirogakatageri.viewservice.util.LifecycleServiceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.Scope
import org.koin.dsl.module

typealias PermissionLauncher = ActivityResultLauncher<Array<out String>>

val chatModule = module {

    factory { FireChatRepository(get()) }

    scope<FireChatFragment> {
        scoped { FireChatMsgController() }
    }

    viewModel { FireChatViewModel(get()) }
}

val securityModule = module {

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

    viewModel { OAuthViewModel(get()) }
}

val mainModule = module {

    single { FirebaseManager() }
    single { FeatureManager() }

    factory { Clock() }

    viewModel { ParentViewModel() }
    viewModel { (state: SavedStateHandle) -> FeatureViewModel(state, get(), get()) }
    viewModel { TimeViewModel(get()) }
    viewModel { FcmViewModel() }

    scope<MainActivity> {
        fragment { FeatureFragment() }
        fragment { TimeFragment() }
        fragment { OAuthFragment() }
    }

    scope<FeatureFragment> {
        scoped {
            val featureList = get<FeatureManager>().featureList
            FeatureAdapter(featureList)
        }
    }

    scope<TimeFragment> {}
    scope<OAuthFragment> {}
    scope<FcmFragment> {}
}

val viewServiceModule = module {

    factory { ServiceViewModelFactory() }
    factory { ServiceViewFactory(get()) }

    scope<SampleViewService> {
        scoped { LifecycleServiceProvider(get()) }
        scoped { ServiceStateModel() }
        scoped { ServiceBroadcastReceiver() }
        scoped<ProfileView> { serviceViewFactory.create(ServiceViews.PROFILE, get(), get()) }
    }
}

val apiModule = module {

    single { ApiClient(androidContext()) }
    single { get<ApiClient>().createApiService() }

    factory { ApiRepository(get()) }
}

val Scope.serviceViewFactory get() = get<ServiceViewFactory>()
