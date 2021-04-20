package com.hirogakatageri.core.sample.ui.time

import android.view.ViewGroup
import com.hirogakatageri.core.fragment.CoreViewModelFragment
import com.hirogakatageri.core.sample.databinding.FragmentTimeBinding
import com.hirogakatageri.core.sample.ui.main.MainViewModel
import com.hirogakatageri.core.sample.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TimeFragment : CoreViewModelFragment<FragmentTimeBinding, MainViewModel>() {

    override val vm: MainViewModel by sharedViewModel()

    override fun createBinding(container: ViewGroup?): FragmentTimeBinding =
        FragmentTimeBinding.inflate(layoutInflater, container, false)

    override fun FragmentTimeBinding.bind() {
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

    private suspend fun observeState() = withContext(Dispatchers.Main) {
        vm.state.collect { state ->
            when (state) {
                is UiState.TimeUpdated -> {
                    binding.textTime.text = state.time
                }
                else -> Unit
            }
        }
    }
}