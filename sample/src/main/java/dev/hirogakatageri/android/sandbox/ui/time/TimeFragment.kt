package dev.hirogakatageri.android.sandbox.ui.time

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import dev.hirogakatageri.android.sample.databinding.FragmentTimeBinding
import dev.hirogakatageri.core.fragment.CoreViewModelFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimeFragment : CoreViewModelFragment<FragmentTimeBinding, TimeViewModel>() {

    override val vm: TimeViewModel by viewModel()

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
                is TimeFragmentState.Origin -> updateTimeText(state.time)
                is TimeFragmentState.TimeUpdated -> updateTimeText(state.time)
            }
        }
    }

    private suspend fun updateTimeText(time: String) = withContext(Dispatchers.Main) {
        binding?.textTime?.text = time
    }
}