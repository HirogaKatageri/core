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

package dev.hirogakatageri.viewservice.view

import androidx.annotation.Keep
import androidx.viewbinding.ViewBinding
import dev.hirogakatageri.viewservice.util.LifecycleServiceProvider
import dev.hirogakatageri.viewservice.viewmodel.ServiceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Wrapper Class for ViewBinding with ViewModel
 * @see ViewBindingServiceView
 * */
@Keep
abstract class ViewBindingViewModelServiceView<VB : ViewBinding, out VM : ServiceViewModel>(
    serviceProvider: LifecycleServiceProvider,
    themeResId: Int,
    protected val viewModel: VM
) : ViewBindingServiceView<VB>(serviceProvider, themeResId) {

    override suspend fun onDetach() = withContext(Dispatchers.Main) {
        super.onDetach()
        viewModel.onDetach()
    }
}
