package dev.hirogakatageri.android.sandbox.service.util

import dev.hirogakatageri.android.sandbox.R
import dev.hirogakatageri.android.sandbox.service.ServiceStateModel
import dev.hirogakatageri.android.sandbox.service.ui.ServiceViews
import dev.hirogakatageri.android.sandbox.service.ui.profile.ProfileView
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