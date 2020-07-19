package com.hirogakatageri.base.activity

import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseViewModelActivity<VB : ViewBinding, VM : ViewModel> :
    BaseActivity<VB>() {

    abstract val viewModel: VM

}