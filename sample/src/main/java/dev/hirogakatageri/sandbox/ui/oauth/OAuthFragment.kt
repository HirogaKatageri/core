package dev.hirogakatageri.sandbox.ui.oauth

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.activity.result.contract.ActivityResultContracts
import com.github.ajalt.timberkt.d
import com.google.android.material.snackbar.Snackbar
import dev.hirogakatageri.core.fragment.CoreViewModelFragment
import dev.hirogakatageri.sandbox.databinding.FragmentOauthBinding
import dev.hirogakatageri.sandbox.ui.oauth.OAuthFragmentState.TwitchAuthState
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
                    is TwitchAuthState.AccessTokenReceived -> {
                        processUiState(state.ui)
                        attachTwitchSwitchListener()
                        val accessToken = state.accessToken
                        showSnackbar("Token: $accessToken")
                    }
                    is TwitchAuthState.AuthCancelled -> {
                        processUiState(state.ui)
                        attachTwitchSwitchListener()
                    }
                    is TwitchAuthState.AuthError -> {
                        processUiState(state.ui)
                        attachTwitchSwitchListener()
                        showSnackbar("An error occurred when signing in with Twitch")
                    }
                    is TwitchAuthState.AuthInProgress -> {
                        detachTwitchSwitchListener()
                        processUiState(state.ui)
                    }
                    is TwitchAuthState.AuthIntentCreated -> {
                        detachTwitchSwitchListener()
                        processUiState(state.ui)
                        twitchOAuthIntentLauncher.launch(state.intent)
                    }
                    is TwitchAuthState.ReAuthenticationRequired -> {
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

    private fun processUiState(
        state: OAuthFragmentUiState
    ) = binding {
        switchTwitchAuth.isChecked = state.isTwitchSignedIn
        processTwitchSignInProgress(state.isTwitchAuthInProgress)
    }

    private fun processTwitchSignInProgress(inProgress: Boolean) = binding {
        switchTwitchAuth.isEnabled = !inProgress
        if (inProgress) {
            progressBar.show()
        } else {
            progressBar.hide()
        }
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
