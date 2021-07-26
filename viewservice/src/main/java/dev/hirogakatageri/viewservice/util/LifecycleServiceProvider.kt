/*
 *    Copyright 2021 Gian Patrick Quintana
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package dev.hirogakatageri.viewservice.util

import androidx.annotation.Keep
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleService
import java.lang.ref.WeakReference

/**
 * Provides the Context and Lifecycle of the Service where the view will be attached.
 * */
@Keep
class LifecycleServiceProvider(service: LifecycleService) {

    private val _service: WeakReference<LifecycleService> = WeakReference(service)

    val service
        get() = _service.get() ?: throw RuntimeException("Reference to Service is missing")
}

val LifecycleServiceProvider.lifecycle: Lifecycle get() = service.lifecycle

@Keep
fun LifecycleServiceProvider.addLifecycleObserver(observer: LifecycleObserver) {
    lifecycle.addObserver(observer)
}
