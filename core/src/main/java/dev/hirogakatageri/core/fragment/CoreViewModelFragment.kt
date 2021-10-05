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

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

@Keep
abstract class CoreViewModelFragment<VB : ViewBinding, out VM : ViewModel> : CoreFragment<VB>() {

    /**
     * The ViewModel used in the Fragment.
     * */
    protected abstract val vm: VM

    /**
     * Function to easily access the ViewModel.
     * */
    protected inline fun <T> vm(block: VM.() -> T): T = vm.run(block)
}
