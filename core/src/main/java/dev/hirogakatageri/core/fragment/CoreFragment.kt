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

package dev.hirogakatageri.core.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.scope.ScopeFragment

@Keep
abstract class CoreFragment<VB : ViewBinding> : ScopeFragment() {

    protected var binding: VB? = null

    protected val lifecycleScope get() = viewLifecycleOwner.lifecycleScope

    /**
     * Function to initialize ViewBinding.
     * @return VB the type of ViewBinding used by the Fragment.
     * */
    protected abstract fun createBinding(container: ViewGroup?): VB

    /**
     * Called after initializing ViewBinding in "onCreateView".
     * */
    protected abstract fun VB.bind()

    /**
     * Function to easily access ViewBinding in the Fragment.
     * It will always run in the main thread.
     * It will only run when binding is not null.
     * */
    protected fun binding(func: VB.() -> Unit) = lifecycleScope.launch(Dispatchers.Main) {
            val b = binding
            if (b == null) Log.e(
                this@CoreFragment::class.simpleName,
                "ViewBinding is null, Action won't be run.",
                RuntimeException("ViewBinding is null, Action won't be run.")
            )
            else b.run(func)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = createBinding(container)
        binding?.bind()
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}