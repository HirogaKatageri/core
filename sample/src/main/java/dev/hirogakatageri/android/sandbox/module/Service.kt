package dev.hirogakatageri.android.sandbox.module

import dev.hirogakatageri.android.sandbox.service.SampleViewService
import dev.hirogakatageri.android.sandbox.service.ServiceStateModel
import dev.hirogakatageri.android.sandbox.service.ui.ServiceViews
import dev.hirogakatageri.android.sandbox.service.ui.profile.ProfileView
import dev.hirogakatageri.android.sandbox.service.util.ServiceBroadcastReceiver
import dev.hirogakatageri.android.sandbox.service.util.ServiceViewFactory
import dev.hirogakatageri.android.sandbox.service.util.ServiceViewModelFactory
import dev.hirogakatageri.viewservice.service.CoreViewService
import dev.hirogakatageri.viewservice.util.LifecycleServiceProvider
import org.koin.core.scope.Scope
import org.koin.dsl.module

internal val serviceModule = module {

    factory<ServiceViewModelFactory> {
        ServiceViewModelFactory()
    }
    factory<ServiceViewFactory> {
        ServiceViewFactory(get())
    }

    scope<SampleViewService> {
        scoped<LifecycleServiceProvider> { (service: CoreViewService) ->
            LifecycleServiceProvider(service)
        }
        scoped<ServiceStateModel> { ServiceStateModel() }
        scoped<ServiceBroadcastReceiver> { ServiceBroadcastReceiver() }

        scoped<ProfileView> { serviceViewFactory.create(ServiceViews.PROFILE, get(), get()) }
    }
}

val Scope.serviceViewFactory get() = get<ServiceViewFactory>()