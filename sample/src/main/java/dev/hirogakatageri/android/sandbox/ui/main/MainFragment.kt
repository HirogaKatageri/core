package dev.hirogakatageri.android.sandbox.ui.main

import android.view.ViewGroup
import dev.hirogakatageri.android.sample.databinding.FragmentMainBinding
import dev.hirogakatageri.core.fragment.CoreViewModelFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment : CoreViewModelFragment<FragmentMainBinding, MainViewModel>() {

    override val vm: MainViewModel by sharedViewModel()

    override fun createBinding(container: ViewGroup?): FragmentMainBinding =
        FragmentMainBinding.inflate(layoutInflater, container, false)

    override fun FragmentMainBinding.bind() {
        btnShowTime.setOnClickListener { vm.showTimeFragment() }
    }
}