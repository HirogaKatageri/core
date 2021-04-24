package com.hirogakatageri.core.activity

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

@Keep
abstract class CoreViewModelActivity<VB : ViewBinding, out VM : ViewModel> : CoreActivity<VB>() {

    /**
     * The ViewModel used in the Activity.
     * */
    protected abstract val vm: VM

    /**
     * Function to easily access contents of the ViewModel.
     * */
    protected inline fun <T> vm(func: VM.() -> T) = vm.run(func)

}