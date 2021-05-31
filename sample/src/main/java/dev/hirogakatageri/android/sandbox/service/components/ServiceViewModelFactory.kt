package dev.hirogakatageri.android.sandbox.service.components

import dev.hirogakatageri.android.sandbox.service.ui.profile.ProfileViewModel
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class ServiceViewModelFactory {

    private val map: Map<ServiceViewType, KClass<out ServiceViewModel>> = mapOf(
        ServiceViewType.PROFILE to ProfileViewModel::class
    )

    /**
     * Instantiates a ServiceViewModel by ServiceViewType.
     * @see ServiceViewType
     * @see ServiceViewModel
     * */
    fun create(type: ServiceViewType): ServiceViewModel =
        map[type]?.createInstance() ?: throw RuntimeException("Invalid ServiceViewType")

}