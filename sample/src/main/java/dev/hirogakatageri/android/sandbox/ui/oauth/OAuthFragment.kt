package dev.hirogakatageri.android.sandbox.ui.oauth

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.github.ajalt.timberkt.d
import com.google.android.material.snackbar.Snackbar
import dev.hirogakatageri.android.sample.databinding.FragmentOauthBinding
import dev.hirogakatageri.core.fragment.CoreViewModelFragment
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class OAuthFragment : CoreViewModelFragment<FragmentOauthBinding, OAuthViewModel>() {

    override val vm: OAuthViewModel by viewModel()

    private val twitchOAuthIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            d { "Twitch OAuth Result: $result" }
            vm.verifyAuthRequestAndGetAccessToken(result.data)
        }

    override fun createBinding(container: ViewGroup?): FragmentOauthBinding =
        FragmentOauthBinding.inflate(layoutInflater)

    override fun FragmentOauthBinding.bind() {
        btnStartAuth.setOnClickListener {
            vm.signInTwitch(requireActivity())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            vm.state.collect { state ->
                when (state) {
                    is OAuthFragmentState.Initialized -> Unit
                    is OAuthFragmentState.TwitchOAuthIntentCreated -> {
                        twitchOAuthIntentLauncher.launch(state.intent)
                    }
                    is OAuthFragmentState.TwitchOAuthTokenReceived -> {
                        val accessToken = state.twitchTokenResponse.accessToken
                        showSnackbar("Token: $accessToken")
                    }
                    is OAuthFragmentState.TwitchOAuthError -> {
                        showSnackbar("TwitchOAuthError")
                    }
                }
            }
        }
    }

    private fun showSnackbar(message: String) {
        val root = binding?.root
        if (root != null) {
            Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
        }
    }

}