package com.hirogakatageri.base.activity

import android.os.Bundle
import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.viewbinding.ViewBinding
import com.hirogakatageri.base.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

@Keep
abstract class BaseViewModelActivity<VB : ViewBinding, VM : BaseViewModel> :
    BaseActivity<VB>() {

    abstract val viewModel: VM

    protected inline fun <T> LiveData<T>.observe(crossinline func: (T) -> Unit) {
        observe(this@BaseViewModelActivity) { func(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch { viewModel.start() }
    }
}