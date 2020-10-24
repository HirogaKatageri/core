package com.hirogakatageri.core.fragment

import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.hirogakatageri.core.viewmodel.BaseViewModel

@Keep
abstract class CoreViewModelFragment<VB : ViewBinding, VM : BaseViewModel> : CoreFragment<VB>() {

    abstract val viewModel: VM

    override suspend fun VB.afterBind() {
        viewModel.start()
    }

    protected inline fun <T> LiveData<T>.observe(crossinline func: (T) -> Unit) {
        observe(viewLifecycleOwner) { func(it) }
    }

}