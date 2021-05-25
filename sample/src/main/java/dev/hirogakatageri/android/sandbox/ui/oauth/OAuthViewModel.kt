package dev.hirogakatageri.android.sandbox.ui.oauth

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.hirogakatageri.android.sandbox.util.TwitchClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OAuthViewModel(
    private val twitchClient: TwitchClient
) : ViewModel() {

    private val _state: MutableStateFlow<OAuthFragmentState> =
        MutableStateFlow(OAuthFragmentState.Initialized())

    val state: StateFlow<OAuthFragmentState> = _state

    fun signInTwitch(context: Context) = viewModelScope.launch {
        val intent = twitchClient.buildAuthRequestIntent(context)
        _state.value = OAuthFragmentState.TwitchOAuthIntentCreated(intent = intent)
    }

    fun verifyAuthRequestAndGetAccessToken(intent: Intent?) = viewModelScope.launch {
        twitchClient.verifyAuthRequestAndGetAccessToken(
            intent = intent,
            onSuccess = { response ->
                _state.value =
                    OAuthFragmentState.TwitchOAuthTokenReceived(twitchTokenResponse = response)
            },
            onError = { ex ->
                _state.value = OAuthFragmentState.TwitchOAuthError(twitchTokenException = ex)
            }
        )
    }

}