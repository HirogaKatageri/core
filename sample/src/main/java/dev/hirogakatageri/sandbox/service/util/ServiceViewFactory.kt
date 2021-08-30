package dev.hirogakatageri.sandbox.service.util

import dev.hirogakatageri.sandbox.R
import dev.hirogakatageri.sandbox.service.ServiceStateModel
import dev.hirogakatageri.sandbox.service.ui.ServiceViews
import dev.hirogakatageri.sandbox.service.ui.profile.ProfileView
import dev.hirogakatageri.viewservice.util.LifecycleServiceProvider
import dev.hirogakatageri.viewservice.view.ServiceView

class ServiceViewFactory(
    val viewModelFactory: ServiceViewModelFactory
) {

    val themeResId = R.style.Theme_Core

    inline fun <reified T : ServiceView> create(
        type: ServiceViews,
        serviceProvider: LifecycleServiceProvider,
        serviceModel: ServiceStateModel
    ): T = when (type) {
        ServiceViews.PROFILE -> ProfileView(
            serviceProvider,
            themeResId,
            viewModelFactory.create(type),
            serviceModel
        ) as T
    }
}
