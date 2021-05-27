package dev.hirogakatageri.android.sandbox.ui.oauth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import dev.hirogakatageri.android.sandbox.api.TwitchClient
import dev.hirogakatageri.android.sandbox.ui.oauth.OAuthFragmentState.TwitchAuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OAuthViewModel(
    private val twitchClient: TwitchClient
) : ViewModel() {

    private val _state: MutableStateFlow<OAuthFragmentState> = MutableStateFlow(
        OAuthFragmentState.Initialized(
            ui = OAuthFragmentUiState(
                isTwitchSignedIn = twitchClient.isSignedIn
            )
        ).also {
            d { "Twitch Client Initialized:isSignedIn:${twitchClient.isSignedIn}" }
        }
    )

    val state: StateFlow<OAuthFragmentState> = _state

    val uiState: OAuthFragmentUiState get() = state.value.ui

    fun signInTwitch(context: Context) = viewModelScope.launch {
        mapTwitchAuthInProgress()
        val intent = twitchClient.buildAuthRequestIntent(context)
        mapTwitchOAuthIntentCreated(intent)
    }

    fun signOutOfTwitch() = viewModelScope.launch {
        twitchClient.signOut()
    }

    fun verifyTwitchAuthResponseAndGetAccessToken(result: ActivityResult) = viewModelScope.launch {
        val intent = result.data

        when {
            result.resultCode == Activity.RESULT_CANCELED -> mapTwitchAuthCancelled()
            intent != null -> twitchClient.verifyAuthRequest(
                intent,
                onError = ::mapTwitchAuthError,
                onSuccess = { response ->
                    twitchClient.requestAccessToken(
                        response,
                        ::mapTwitchAuthError,
                        ::mapTwitchAccessTokenReceived
                    )
                }
            )
            else -> e { "Authentication Response is null" }
        }
    }

    private fun mapTwitchAuthInProgress() = viewModelScope.launch {
        _state.value = TwitchAuthState.TwitchAuthInProgress(
            ui = uiState.copy(
                isTwitchAuthInProgress = true,
                isTwitchSignedIn = false
            )
        )
    }

    private fun mapTwitchOAuthIntentCreated(intent: Intent) = viewModelScope.launch {
        _state.value = TwitchAuthState.TwitchAuthIntentCreated(
            intent = intent,
            ui = uiState.copy(
                isTwitchAuthInProgress = false,
                isTwitchSignedIn = false
            )
        )
    }

    private fun mapTwitchAuthError(ex: Exception) = viewModelScope.launch {
        _state.value = TwitchAuthState.TwitchAuthError(
            exception = ex,
            ui = uiState.copy(
                isTwitchAuthInProgress = false,
                isTwitchSignedIn = twitchClient.isSignedIn
            )
        )
    }

    private fun mapTwitchAccessTokenReceived(accessToken: String?) = viewModelScope.launch {
        _state.value = TwitchAuthState.TwitchAccessTokenReceived(
            accessToken = accessToken,
            ui = uiState.copy(
                isTwitchSignedIn = twitchClient.isSignedIn,
                isTwitchAuthInProgress = false
            )
        )
    }

    private fun mapTwitchAuthCancelled() = viewModelScope.launch {
        _state.value = TwitchAuthState.TwitchAuthCancelled(
            ui = uiState.copy(
                isTwitchSignedIn = false,
                isTwitchAuthInProgress = false
            )
        )
    }

}