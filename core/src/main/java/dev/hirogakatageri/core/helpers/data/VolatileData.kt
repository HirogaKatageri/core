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

package dev.hirogakatageri.core.helpers.data

import androidx.annotation.Keep
import java.lang.ref.WeakReference

/**
 * Helper class to hold a weak reference to a value.
 * It will remove the reference after being read at least once.
 * */
@Keep
open class VolatileData<out T>(
    value: T? = null,
) {
    private val reference = WeakReference(value)

    fun get(): T? {
        val value = reference.get()

        reference.clear()
        return value
    }
}