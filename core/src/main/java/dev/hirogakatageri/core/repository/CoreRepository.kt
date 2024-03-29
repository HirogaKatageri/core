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

package dev.hirogakatageri.core.repository

import androidx.annotation.Keep
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

@Keep
abstract class CoreRepository(
    protected val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    /**
     * By default will use defaultDispatcher injected on Repository.
     * */
    protected suspend fun <T> withDefaultContext(
        context: CoroutineContext = defaultDispatcher,
        block: suspend CoroutineScope.() -> T,
    ) = withContext(context, block)
}
