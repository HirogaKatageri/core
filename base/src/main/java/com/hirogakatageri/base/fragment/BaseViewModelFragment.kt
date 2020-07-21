package com.hirogakatageri.base.fragment

import androidx.viewbinding.ViewBinding
import com.hirogakatageri.base.activity.BaseViewModelActivity
import com.hirogakatageri.base.viewmodel.BaseViewModel

abstract class BaseViewModelFragment<VB : ViewBinding, VM : BaseViewModel> : BaseFragment<VB>() {

    abstract val viewModel: VM

    inline fun <reified T : BaseViewModel> withActivityViewModel(func: T.() -> Unit) {
        (requireActivity() as BaseViewModelActivity<*, T>).viewModel.run(func)
    }

}