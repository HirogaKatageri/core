package com.hirogakatageri.core.sample.ui.main

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.github.ajalt.timberkt.Timber.d
import com.hirogakatageri.core.activity.CoreViewModelActivity
import com.hirogakatageri.core.sample.databinding.ActivityMainBinding
import com.hirogakatageri.core.sample.ui.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : CoreViewModelActivity<ActivityMainBinding, MainViewModel>() {

    override val vm: MainViewModel by viewModel()

    private val navController: NavController get() = binding.fragmentContainer.findNavController()

    override fun createBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun ActivityMainBinding.bind() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            // It is best to wrap StateFlow.collect() in launch()
            // In order to avoid blocking the thread.
            launch { observeState() }
        }
    }

    private suspend fun observeState() = withContext(Dispatchers.Main) {
        vm.state.collect { state ->
            when (state) {
                is ScreenState.MainScreen -> Unit
                is ScreenState.TimeScreen -> navController.navigate(MainFragmentDirections.mainScreenToTimeScreen())
                else -> Unit
            }
        }
    }
}