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

package dev.hirogakatageri.core.activity

import android.os.Bundle
import androidx.annotation.Keep
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job
import org.koin.androidx.scope.ScopeActivity

@Keep
abstract class CoreActivity<VB : ViewBinding> : ScopeActivity() {

    /**
     * The ViewBinding property used by the Activity.
     * */
    protected lateinit var binding: VB

    /**
     * Function to initialize ViewBinding.
     * @return ViewBinding used by the Activity.
     * */
    protected abstract fun createBinding(): VB

    /**
     * Called after [createBinding] and [setContentView] in [onCreate].
     * Initialization of UI is recommended here.
     * */
    protected abstract fun VB.bind()

    /**
     * Function to easily manipulate ViewBinding used in Activity.
     * It runs in the Main thread and if Lifecycle State is at least [Lifecycle.State.STARTED]
     * */
    protected fun binding(func: VB.() -> Unit): Job = lifecycleScope.launchWhenStarted {
        binding.run(func)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createBinding()
        setContentView(binding.root)
        binding.bind()
    }
}
