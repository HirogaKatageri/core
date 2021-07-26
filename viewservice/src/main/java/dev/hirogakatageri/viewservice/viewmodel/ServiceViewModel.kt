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

package dev.hirogakatageri.viewservice.viewmodel

import android.util.Log
import androidx.annotation.CallSuper
import androidx.annotation.Keep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import java.util.concurrent.CancellationException
import kotlin.coroutines.CoroutineContext

@Keep
abstract class ServiceViewModel : CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    /**
     * Callback method to notify the ViewModel the associated View has been detached from the
     * WindowManager. By default this method is called during the onDetach of AbstractServiceView.
     * This will cancel all Coroutine Jobs with CancellationException("View is detached").
     * */
    @CallSuper
    open fun onDetach() {
        Log.d(
            this@ServiceViewModel::class.simpleName,
            "Detached"
        )
        job.cancelChildren(CancellationException("View is detached"))
    }
}
