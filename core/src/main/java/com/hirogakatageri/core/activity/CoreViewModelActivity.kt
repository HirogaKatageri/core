package com.hirogakatageri.core.activity

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

@Keep
abstract class CoreViewModelActivity<VB : ViewBinding, VM : ViewModel> : CoreActivity<VB>() {

    abstract val vm: VM

    protected inline fun <T> vm(func: VM.() -> T) = vm.run(func)


}