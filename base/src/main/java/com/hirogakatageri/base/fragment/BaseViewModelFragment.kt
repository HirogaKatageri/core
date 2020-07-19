package com.hirogakatageri.base.fragment

import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseViewModelFragment<VB : ViewBinding, VM : ViewModel> : BaseFragment<VB>() {

    abstract val viewModel: VM

}