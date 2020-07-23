package com.hirogakatageri.base.activity

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.viewbinding.ViewBinding
import com.hirogakatageri.base.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

abstract class BaseViewModelActivity<VB : ViewBinding, VM : BaseViewModel> :
    BaseActivity<VB>() {

    abstract val viewModel: VM

    inline fun <T> LiveData<T>.observe(crossinline func: suspend (T) -> Unit) {
        observe(this@BaseViewModelActivity) { launch { func(it) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch { viewModel.start() }
    }
}