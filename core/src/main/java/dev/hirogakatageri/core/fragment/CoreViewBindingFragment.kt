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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import org.koin.androidx.scope.ScopeFragment

@Keep
abstract class CoreViewBindingFragment<T : ViewBinding> :
    ScopeFragment(),
    ViewBindingFragment<T> {

    private val viewBindingLifecycleObserver = ViewBindingLifecycleObserver()

    override var binding: T? = null

    /**
     * Function to initialize ViewBinding.
     * @return ViewBinding used by the Fragment.
     * */
    protected abstract fun createBinding(container: ViewGroup?): T

    /**
     * Called after [createBinding] in [onCreateView].
     * Initialization of UI is recommended here.
     * */
    protected abstract fun T.bind()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = createBinding(container).also { vb ->
        binding = vb
        vb.bind()
        viewLifecycleOwner.lifecycle.addObserver(viewBindingLifecycleObserver)
    }.root

    private inner class ViewBindingLifecycleObserver : DefaultLifecycleObserver {

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            clearBinding()
        }
    }
}
