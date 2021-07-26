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

package dev.hirogakatageri.core.service

import androidx.annotation.Keep
import androidx.lifecycle.LifecycleService
import org.koin.android.scope.AndroidScopeComponent
import org.koin.android.scope.serviceScope
import org.koin.core.scope.Scope

/**
 * This service is based on ScopeService made in Koin v2.x.x
 * With a difference where this implements LifecycleService.
 * */
@Keep
abstract class CoreService(
    private val initialiseScope: Boolean = true
) : LifecycleService(), AndroidScopeComponent {

    override val scope: Scope by serviceScope()

    override fun onCreate() {
        super.onCreate()

        if (initialiseScope) {
            scope.logger.debug("Open Service Scope: $scope")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        scope.logger.debug("Close service scope: $scope")
        if (!scope.closed)
            scope.close()
    }
}
