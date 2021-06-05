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
import kotlinx.coroutines.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.Scope
import org.koin.core.scope.newScope

/**
 * This service is based on ScopeService made in Koin v2.x.x
 * With a difference where this implements LifecycleService.
 * */
@Keep
abstract class CoreService(
    private val initialiseScope: Boolean = true
) : LifecycleService(),
    KoinScopeComponent {

    override val scope: Scope by lazy { newScope(this) }

    override fun onCreate() {
        super.onCreate()

        if (initialiseScope) {
            getKoin().logger.debug("Open Service Scope: $scope")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        getKoin().logger.debug("Close service scope: $scope")
        scope.close()
    }

    /**
     * inject lazily
     * @param qualifier - bean qualifier / optional
     * @param mode
     * @param parameters - injection parameters
     */
    inline fun <reified T : Any> inject(
        qualifier: Qualifier? = null,
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
        noinline parameters: ParametersDefinition? = null
    ) = lazy(mode) { get<T>(qualifier, parameters) }

    /**
     * get given dependency
     * @param name - bean name
     * @param scope
     * @param parameters - injection parameters
     */
    inline fun <reified T : Any> get(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null
    ): T = scope.get(qualifier, parameters)
}