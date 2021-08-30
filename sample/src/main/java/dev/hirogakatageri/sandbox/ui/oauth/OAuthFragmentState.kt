package dev.hirogakatageri.sandbox.ui.oauth

import android.content.Intent

data class OAuthFragmentUiState(
    val isTwitchSignedIn: Boolean = false,
    val isTwitchAuthInProgress: Boolean = false
)

sealed class OAuthFragmentState {

    abstract val ui: OAuthFragmentUiState

    data class Initialized(
        override val ui: OAuthFragmentUiState
    ) : OAuthFragmentState()

    sealed class TwitchAuthState : OAuthFragmentState() {

        data class AuthInProgress(
            override val ui: OAuthFragmentUiState
        ) : TwitchAuthState()

        data class AuthIntentCreated(
            override val ui: OAuthFragmentUiState,
            val intent: Intent
        ) : TwitchAuthState()

        data class AuthError(
            override val ui: OAuthFragmentUiState,
            val exception: Exception
        ) : TwitchAuthState()

        data class AccessTokenReceived(
            override val ui: OAuthFragmentUiState,
            val accessToken: String?
        ) : TwitchAuthState()

        data class ReAuthenticationRequired(
            override val ui: OAuthFragmentUiState
        ) : TwitchAuthState()

        data class AuthCancelled(
            override val ui: OAuthFragmentUiState
        ) : TwitchAuthState()
    }
}
