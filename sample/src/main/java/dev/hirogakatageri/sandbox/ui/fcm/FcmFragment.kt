package dev.hirogakatageri.sandbox.ui.fcm

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import dev.hirogakatageri.core.fragment.CoreViewModelFragment
import dev.hirogakatageri.sandbox.R
import dev.hirogakatageri.sandbox.databinding.FragmentFcmBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class FcmFragment : CoreViewModelFragment<FragmentFcmBinding, FcmViewModel>() {

    private val args: FcmFragmentArgs by navArgs()

    override val vm: FcmViewModel by viewModel()

    override fun createBinding(container: ViewGroup?): FragmentFcmBinding =
        FragmentFcmBinding.inflate(layoutInflater, container, false)

    override fun FragmentFcmBinding.bind() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            launch { observeState() }
            launch { vm.setArgs(args) }
        }
    }

    private suspend fun observeState() = withContext(Dispatchers.Main) {
        vm.state.collect { state ->
            when (state) {
                is FcmFragmentState.Neutral -> onNeutralState()
                is FcmFragmentState.NotificationReceived -> onNotificationReceived(state.message)
            }
        }
    }

    private suspend fun onNeutralState() = withContext(Dispatchers.Main) {
        binding?.txtMessage?.text = getString(R.string.fcm_empty_notification)
    }

    private suspend fun onNotificationReceived(message: String) = withContext(Dispatchers.Main) {
        binding?.txtMessage?.text = getString(R.string.fcm_template_message, message)
    }
}
