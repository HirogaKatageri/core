package dev.hirogakatageri.android.sandbox.ui.oauth

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

        data class TwitchAuthInProgress(
            override val ui: OAuthFragmentUiState
        ) : TwitchAuthState()

        data class TwitchAuthIntentCreated(
            override val ui: OAuthFragmentUiState,
            val intent: Intent
        ) : TwitchAuthState()

        data class TwitchAuthError(
            override val ui: OAuthFragmentUiState,
            val exception: Exception
        ) : TwitchAuthState()

        data class TwitchAccessTokenReceived(
            override val ui: OAuthFragmentUiState,
            val accessToken: String?
        ) : TwitchAuthState()

        data class TwitchAuthRequired(
            override val ui: OAuthFragmentUiState
        ) : TwitchAuthState()

        data class TwitchAuthCancelled(
            override val ui: OAuthFragmentUiState
        ) : TwitchAuthState()

    }
}