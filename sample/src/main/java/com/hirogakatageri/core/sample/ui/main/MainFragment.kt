package com.hirogakatageri.core.sample.ui.main

import android.view.ViewGroup
import com.hirogakatageri.core.fragment.CoreViewModelFragment
import com.hirogakatageri.core.sample.databinding.FragmentMainBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment : CoreViewModelFragment<FragmentMainBinding, MainViewModel>() {

    override val vm: MainViewModel by sharedViewModel()

    override fun createBinding(container: ViewGroup?): FragmentMainBinding =
        FragmentMainBinding.inflate(layoutInflater, container, false)

    override fun FragmentMainBinding.bind() {
        btnShowTime.setOnClickListener { vm.showTimeFragment() }
    }
}