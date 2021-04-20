package com.hirogakatageri.core.sample.ui.main

import android.view.ViewGroup
import com.hirogakatageri.core.fragment.CoreViewModelFragment
import com.hirogakatageri.core.sample.databinding.FragmentSampleBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SampleFragment : CoreViewModelFragment<FragmentSampleBinding, SampleViewModel>() {

    override val vm: SampleViewModel by sharedViewModel()

    override fun createBinding(container: ViewGroup?): FragmentSampleBinding =
        FragmentSampleBinding.inflate(layoutInflater, container, false)

    override fun FragmentSampleBinding.bind() {
        lifecycleScope.launchWhenStarted {
            observeState()
        }
    }

    override fun onResume() {
        super.onResume()
        vm.startTimer()
    }

    override fun onStop() {
        super.onStop()
        vm.stopTimer()
    }

    private suspend fun observeState() = lifecycleScope.launch(Dispatchers.Main) {
        vm.state.collect { state ->
            when (state) {
                is SampleState.Created -> Unit
                is SampleState.TimeUpdated -> {
                    binding.textView.text = state.time
                }
            }
        }
    }
}