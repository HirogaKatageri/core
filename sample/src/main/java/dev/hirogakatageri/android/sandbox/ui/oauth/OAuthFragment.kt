package dev.hirogakatageri.android.sandbox.ui.oauth

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.activity.result.contract.ActivityResultContracts
import com.github.ajalt.timberkt.d
import com.google.android.material.snackbar.Snackbar
import dev.hirogakatageri.android.sample.databinding.FragmentOauthBinding
import dev.hirogakatageri.android.sandbox.ui.oauth.OAuthFragmentState.TwitchAuthState
import dev.hirogakatageri.core.fragment.CoreViewModelFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class OAuthFragment : CoreViewModelFragment<FragmentOauthBinding, OAuthViewModel>() {

    override val vm: OAuthViewModel by viewModel()

    private val twitchOAuthIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            vm.verifyTwitchAuthResponseAndGetAccessToken(result)
        }

    override fun createBinding(container: ViewGroup?): FragmentOauthBinding =
        FragmentOauthBinding.inflate(layoutInflater)

    override fun FragmentOauthBinding.bind() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            vm.state.collect { state ->
                d { "State: ${state::class.simpleName}" }

                when (state) {
                    is OAuthFragmentState.Initialized -> {
                        processUiState(state.ui)
                        attachTwitchSwitchListener()
                    }
                    is TwitchAuthState.TwitchAccessTokenReceived -> {
                        processUiState(state.ui)
                        attachTwitchSwitchListener()
                        val accessToken = state.accessToken
                        showSnackbar("Token: $accessToken")
                    }
                    is TwitchAuthState.TwitchAuthCancelled -> {
                        processUiState(state.ui)
                        attachTwitchSwitchListener()
                    }
                    is TwitchAuthState.TwitchAuthError -> {
                        processUiState(state.ui)
                        attachTwitchSwitchListener()
                        showSnackbar("An error occurred when signing in with Twitch")
                    }
                    is TwitchAuthState.TwitchAuthInProgress -> {
                        detachTwitchSwitchListener()
                        processUiState(state.ui)
                    }
                    is TwitchAuthState.TwitchAuthIntentCreated -> {
                        detachTwitchSwitchListener()
                        twitchOAuthIntentLauncher.launch(state.intent)
                    }
                    is TwitchAuthState.TwitchAuthRequired -> {
                        processUiState(state.ui)
                        attachTwitchSwitchListener()
                        showSnackbar("Your session with Twitch has expired")
                    }
                }
            }
        }
    }

    private fun attachTwitchSwitchListener() = binding {
        switchTwitchAuth.setOnCheckedChangeListener(TwitchSwitchStateListener())
    }

    private fun detachTwitchSwitchListener() = binding {
        switchTwitchAuth.setOnCheckedChangeListener(null)
    }

    private suspend fun processUiState(
        state: OAuthFragmentUiState
    ) = withContext(Dispatchers.Main) {
        binding?.switchTwitchAuth?.isChecked = state.isTwitchSignedIn
        binding?.switchTwitchAuth?.isEnabled = !state.isTwitchAuthInProgress
    }

    private suspend fun showSnackbar(message: String) = withContext(Dispatchers.Main) {
        val root = binding?.root
        if (root != null) {
            Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private inner class TwitchSwitchStateListener : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            if (buttonView?.isEnabled == true) {
                d { "Twitch process activated" }
                if (isChecked) vm.signInTwitch(requireActivity())
                else vm.signOutOfTwitch()
            }
        }
    }

}