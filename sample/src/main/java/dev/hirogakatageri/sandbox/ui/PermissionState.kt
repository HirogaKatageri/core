package dev.hirogakatageri.sandbox.ui

sealed class PermissionState {

    abstract val state: Int

    data class Neutral(override val state: Int = 0) : PermissionState()

    data class ServiceViewPermissionsGranted(override val state: Int = 1) : PermissionState()
    data class ServiceViewPermissionsDenied(override val state: Int = 2) : PermissionState()

    data class OverlayPermissionGranted(override val state: Int = 3) : PermissionState()
    data class OverlayPermissionDenied(override val state: Int = 4) : PermissionState()
}
