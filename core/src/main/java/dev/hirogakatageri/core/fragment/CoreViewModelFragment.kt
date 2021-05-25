package dev.hirogakatageri.core.fragment

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

@Keep
abstract class CoreViewModelFragment<VB : ViewBinding, out VM : ViewModel> : CoreFragment<VB>() {

    abstract val vm: VM

    protected inline fun <T> vm(func: VM.() -> T) = vm.run(func)

}