package dev.hirogakatageri.android.sandbox.ui.oauth

import android.content.Intent
import net.openid.appauth.TokenResponse

sealed class OAuthFragmentState {

    abstract val state: Int
    open val intent: Intent? = null
    open val twitchTokenResponse: TokenResponse? = null
    open val twitchTokenException: Exception? = null

    class Initialized(override val state: Int = 0) : OAuthFragmentState()

    class TwitchOAuthIntentCreated(
        override val state: Int = 1,
        override val intent: Intent
    ) : OAuthFragmentState()

    class TwitchOAuthTokenReceived(
        override val state: Int = 2,
        override val twitchTokenResponse: TokenResponse
    ) : OAuthFragmentState()

    class TwitchOAuthError(
        override val state: Int = 3,
        override val twitchTokenException: Exception
    ) : OAuthFragmentState()

}
