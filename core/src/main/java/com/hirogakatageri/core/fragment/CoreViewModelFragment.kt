package com.hirogakatageri.core.fragment

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

@Keep
abstract class CoreViewModelFragment<VB : ViewBinding, VM : ViewModel> : CoreFragment<VB>() {

    abstract val viewModel: VM

    protected inline fun <T> vm(func: VM.() -> T) = viewModel.run(func)

}