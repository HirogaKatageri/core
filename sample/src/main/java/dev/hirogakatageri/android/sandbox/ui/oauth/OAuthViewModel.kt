package dev.hirogakatageri.android.sandbox.ui.oauth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import dev.hirogakatageri.android.sandbox.ui.oauth.OAuthFragmentState.TwitchAuthState
import dev.hirogakatageri.oauth2client.twitch.TwitchClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationResponse

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
        val intent = twitchClient.buildAuthRequestIntent(
            context,
            Uri.parse("https://sandbox.android.hirogakatageri.dev/oauth2redirect/twitch")
        )
        mapTwitchOAuthIntentCreated(intent)
    }

    fun signOutOfTwitch() = viewModelScope.launch {
        twitchClient.signOut()
    }

    fun verifyTwitchAuthResponseAndGetAccessToken(result: ActivityResult) = viewModelScope.launch {
        val intent = result.data

        when {
            result.resultCode == Activity.RESULT_CANCELED -> mapTwitchAuthCancelled()
            intent != null -> twitchClient.verifyAuthResponse(
                intent,
                onError = ::mapTwitchAuthError,
                onSuccess = ::requestAccessToken
            )
            else -> e { "Authentication Response is null" }
        }
    }

    private fun requestAccessToken(
        response: AuthorizationResponse
    ) = viewModelScope.launch {
        twitchClient.requestAccessToken(
            response,
            ::mapTwitchAuthError,
            ::mapTwitchAccessTokenReceived
        )
    }

    private fun mapTwitchAuthInProgress() = viewModelScope.launch {
        _state.value = TwitchAuthState.AuthInProgress(
            ui = uiState.copy(
                isTwitchAuthInProgress = true,
                isTwitchSignedIn = false
            )
        )
    }

    private fun mapTwitchOAuthIntentCreated(intent: Intent) = viewModelScope.launch {
        _state.value = TwitchAuthState.AuthIntentCreated(
            intent = intent,
            ui = uiState.copy(
                isTwitchAuthInProgress = true,
                isTwitchSignedIn = false
            )
        )
    }

    private fun mapTwitchAuthError(ex: Exception) = viewModelScope.launch {
        _state.value = TwitchAuthState.AuthError(
            exception = ex,
            ui = uiState.copy(
                isTwitchAuthInProgress = false,
                isTwitchSignedIn = twitchClient.isSignedIn
            )
        )
    }

    private fun mapTwitchAccessTokenReceived(accessToken: String?) = viewModelScope.launch {
        _state.value = TwitchAuthState.AccessTokenReceived(
            accessToken = accessToken,
            ui = uiState.copy(
                isTwitchSignedIn = twitchClient.isSignedIn,
                isTwitchAuthInProgress = false
            )
        )
    }

    private fun mapTwitchAuthCancelled() = viewModelScope.launch {
        _state.value = TwitchAuthState.AuthCancelled(
            ui = uiState.copy(
                isTwitchSignedIn = false,
                isTwitchAuthInProgress = false
            )
        )
    }

}