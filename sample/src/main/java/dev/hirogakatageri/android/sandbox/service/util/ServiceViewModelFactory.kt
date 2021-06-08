package dev.hirogakatageri.android.sandbox.service.util

import dev.hirogakatageri.android.sandbox.service.ui.ServiceViews
import dev.hirogakatageri.android.sandbox.service.ui.profile.ProfileViewModel
import dev.hirogakatageri.viewservice.viewmodel.ServiceViewModel

class ServiceViewModelFactory {

    inline fun <reified T : ServiceViewModel> create(type: ServiceViews): T =
        when (type) {
            ServiceViews.PROFILE -> ProfileViewModel() as T
        }
}