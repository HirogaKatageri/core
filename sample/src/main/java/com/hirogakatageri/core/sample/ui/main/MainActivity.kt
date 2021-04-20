package com.hirogakatageri.core.sample.ui.main

import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.github.ajalt.timberkt.d
import com.hirogakatageri.core.activity.CoreViewModelActivity
import com.hirogakatageri.core.sample.databinding.ActivityMainBinding
import com.hirogakatageri.core.sample.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : CoreViewModelActivity<ActivityMainBinding, MainViewModel>() {

    override val vm: MainViewModel by viewModel()

    override fun createBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun ActivityMainBinding.bind() {
        lifecycleScope.launchWhenStarted {
            observeState()
        }
    }

    private suspend fun observeState() = withContext(Dispatchers.Main) {
        vm.state.collect { state ->
            when (state) {
                is UiState.MainFragmentStarting -> {
                    d { "TODO UiState.MainFragmentStarting" }
                }
                is UiState.TimeFragmentStarting -> {
                    val action = MainFragmentDirections.actionMainFragmentToTimeFragment()
                    binding.fragmentContainer.findNavController().navigate(action)
                }
                else -> Unit
            }
        }
    }
}