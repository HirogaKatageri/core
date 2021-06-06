package dev.hirogakatageri.android.sandbox.service.util

import dev.hirogakatageri.android.sandbox.service.ViewService
import java.lang.ref.WeakReference

class ServiceProvider(service: ViewService) {

    private val _service: WeakReference<ViewService> = WeakReference(service)

    fun getService(): ViewService =
        _service.get() ?: throw RuntimeException("Reference to Service is missing")
}