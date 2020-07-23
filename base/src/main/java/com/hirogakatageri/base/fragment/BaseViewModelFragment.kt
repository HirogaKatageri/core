package com.hirogakatageri.base.fragment

import androidx.viewbinding.ViewBinding
import com.hirogakatageri.base.activity.BaseViewModelActivity
import com.hirogakatageri.base.viewmodel.BaseViewModel

abstract class BaseViewModelFragment<VB : ViewBinding, VM : BaseViewModel> : BaseFragment<VB>() {

    abstract val viewModel: VM

    override suspend fun afterBind() {
        viewModel.start()
    }

    inline fun <reified T : BaseViewModel> withActivityViewModel(func: T.() -> Unit) {
        val viewModelActivity = activity

        if (viewModelActivity is BaseViewModelActivity<*, *>) viewModelActivity.viewModel.let {
            if (it is T) it.run(func)
        }
    }

}