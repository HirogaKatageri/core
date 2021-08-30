package dev.hirogakatageri.sandbox.service.util

import dev.hirogakatageri.sandbox.service.ui.ServiceViews
import dev.hirogakatageri.sandbox.service.ui.profile.ProfileViewModel
import dev.hirogakatageri.viewservice.viewmodel.ServiceViewModel

class ServiceViewModelFactory {

    inline fun <reified T : ServiceViewModel> create(type: ServiceViews): T =
        when (type) {
            ServiceViews.PROFILE -> ProfileViewModel() as T
        }
}
