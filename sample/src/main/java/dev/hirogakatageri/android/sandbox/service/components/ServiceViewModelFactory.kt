package dev.hirogakatageri.android.sandbox.service.components

import dev.hirogakatageri.android.sandbox.service.ui.profile.ProfileViewModel
import kotlin.reflect.KClass

class ServiceViewModelFactory {

    /**
     * Instantiates a ServiceViewModel by ServiceViewType.
     * @see ServiceViewType
     * @see ServiceViewModel
     * */
    inline fun <reified T : ServiceViewModel> create(type: KClass<T>): T =
        when (type) {
            ProfileViewModel::class -> ProfileViewModel() as T
            else -> throw RuntimeException(""""type" is not of ServiceViewModel""")
        }
}