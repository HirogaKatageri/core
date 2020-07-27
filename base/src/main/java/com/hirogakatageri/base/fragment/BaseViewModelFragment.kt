package com.hirogakatageri.base.fragment

import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.viewbinding.ViewBinding
import com.hirogakatageri.base.activity.BaseViewModelActivity
import com.hirogakatageri.base.viewmodel.BaseViewModel

@Keep
abstract class BaseViewModelFragment<VB : ViewBinding, VM : BaseViewModel> : BaseFragment<VB>() {

    abstract val viewModel: VM

    override suspend fun afterBind() {
        viewModel.start()
    }

    protected inline fun <reified T : BaseViewModel> withActivityViewModel(func: T.() -> Unit) {
        val viewModelActivity = activity

        if (viewModelActivity is BaseViewModelActivity<*, *>) viewModelActivity.viewModel.let {
            if (it is T) it.run(func)
        }
    }

    protected inline fun <T> LiveData<T>.observe(crossinline func: (T) -> Unit) {
        observe(viewLifecycleOwner) { func(it) }
    }

}