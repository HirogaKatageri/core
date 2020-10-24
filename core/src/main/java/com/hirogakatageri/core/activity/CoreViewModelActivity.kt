package com.hirogakatageri.core.activity

import android.os.Bundle
import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.hirogakatageri.core.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

@Keep
abstract class CoreViewModelActivity<VB : ViewBinding, VM : BaseViewModel> :
    CoreActivity<VB>() {

    abstract val viewModel: VM

    protected inline fun <T> LiveData<T>.observe(crossinline func: (T) -> Unit) {
        observe(this@CoreViewModelActivity) { func(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch { viewModel.start() }
    }
}