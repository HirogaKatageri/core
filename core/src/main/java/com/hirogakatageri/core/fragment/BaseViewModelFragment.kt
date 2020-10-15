package com.hirogakatageri.core.fragment

import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.viewbinding.ViewBinding
import com.hirogakatageri.core.viewmodel.BaseViewModel

@Keep
abstract class BaseViewModelFragment<VB : ViewBinding, VM : BaseViewModel> : BaseFragment<VB>() {

    abstract val viewModel: VM

    override suspend fun VB.afterBind() {
        viewModel.start()
    }

    protected inline fun <T> LiveData<T>.observe(crossinline func: (T) -> Unit) {
        observe(viewLifecycleOwner) { func(it) }
    }

}