package com.hirogakatageri.core.sample.ui.time

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.hirogakatageri.core.fragment.CoreViewModelFragment
import com.hirogakatageri.core.sample.databinding.FragmentTimeBinding
import com.hirogakatageri.core.sample.ui.ScreenState
import com.hirogakatageri.core.sample.ui.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TimeFragment : CoreViewModelFragment<FragmentTimeBinding, MainViewModel>() {

    override val vm: MainViewModel by sharedViewModel()

    override fun createBinding(container: ViewGroup?): FragmentTimeBinding =
        FragmentTimeBinding.inflate(layoutInflater, container, false)

    override fun FragmentTimeBinding.bind() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // For Fragments it's recommended to observe state in the onViewCreated function.
        lifecycleScope.launchWhenStarted {
            // It is best to wrap StateFlow.collect() in launch()
            // In order to avoid blocking the thread.
            launch { observeState() }
        }

        vm.attachClock(viewLifecycleOwner)
    }

    private suspend fun observeState() = withContext(Dispatchers.Main) {
        vm.state.collect { state ->
            when (state) {
                is ScreenState.TimeUpdated -> {
                    binding?.textTime?.text = state.time
                }
                else -> Unit
            }
        }
    }
}