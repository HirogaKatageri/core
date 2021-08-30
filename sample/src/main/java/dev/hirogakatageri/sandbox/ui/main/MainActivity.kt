package dev.hirogakatageri.sandbox.ui.main

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.github.ajalt.timberkt.Timber.d
import dev.hirogakatageri.core.activity.CoreViewModelActivity
import dev.hirogakatageri.sandbox.databinding.ActivityMainBinding
import dev.hirogakatageri.sandbox.ui.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : CoreViewModelActivity<ActivityMainBinding, ParentViewModel>() {

    override val vm: ParentViewModel by viewModel()

    private val navController: NavController get() = binding.fragmentContainer.findNavController()

    override fun createBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun ActivityMainBinding.bind() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            // It is best to wrap StateFlow.collect() in launch()
            // In order to avoid blocking the thread.
            navController.addOnDestinationChangedListener { _, destination, _ ->
                vm.onNavigationDestinationChanged(destination)
            }
            launch { observeState() }
            launch { checkIntent() }
        }
    }

    private suspend fun observeState() = withContext(Dispatchers.Main) {
        vm.state.collect { state ->
            when (state) {
                is ScreenState.MainScreen -> Unit
                is ScreenState.TimeScreen -> navController.navigate(MainFragmentDirections.mainScreenToTimeScreen())
                is ScreenState.OAuthScreen -> navController.navigate(MainFragmentDirections.mainScreenToOauthScreen())
                is ScreenState.FCMScreen -> navController.navigate(
                    MainFragmentDirections.mainScreenToFcmScreen(state.message)
                )
            }
        }
    }

    private suspend fun checkIntent() = withContext(Dispatchers.Main) {
        val uri = intent.data

        if (uri != null) {
            when (uri.path) {
                "/fcm" -> {
                    val message = uri.getQueryParameter("message") ?: ""
                    vm.showFcmFragment(message)
                }
                else -> d { "URI path not supported." }
            }
        }
    }
}
