package dev.hirogakatageri.oauth2client

import android.content.Context
import androidx.annotation.Keep
import androidx.core.content.edit
import dev.hirogakatageri.oauth2client.TwitchClient.Companion.TWITCH_AUTH_STATE_KEY
import dev.hirogakatageri.oauth2client.base.AbstractOAuthClient
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.TokenRequest

@Keep
fun OAuthPreferences.getCachedTwitchState(config: AuthorizationServiceConfiguration): AuthState =
    preferences.getString(TWITCH_AUTH_STATE_KEY, null)?.let { stateJson ->
        AuthState.jsonDeserialize(stateJson)
    } ?: AuthState(config)

@Keep
fun OAuthPreferences.updateCachedTwitchState(state: AuthState) {
    preferences.edit {
        putString(TWITCH_AUTH_STATE_KEY, state.jsonSerializeString())
    }
}

@Keep
class TwitchClient : AbstractOAuthClient {

    constructor(
        authorizationUrl: String,
        tokenUrl: String,
        clientId: String,
        scope: String,
        preferences: OAuthPreferences,
        secretKey: String
    ) : super(authorizationUrl, tokenUrl, clientId, scope, preferences) {
        this.secretKey = secretKey
    }

    constructor(
        context: Context,
        authorizationUrl: String,
        tokenUrl: String,
        clientId: String,
        scope: String,
        preferences: OAuthPreferences,
        secretKey: String
    ) : super(context, authorizationUrl, tokenUrl, clientId, scope, preferences) {
        this.secretKey = secretKey
    }

    private val secretKey: String

    override val defaultAuthRequestParams: Map<String, String>
        get() = mapOf(
            "force_verify" to "true"
        )

    override fun initializeAuthState() {
        authState = preferences.getCachedTwitchState(serviceConfig)
    }

    override fun onAuthStateUpdated(newState: AuthState) {
        super.onAuthStateUpdated(newState)
        preferences.updateCachedTwitchState(newState)
    }

    override fun AuthorizationResponse.buildTokenExchangeRequest(): TokenRequest {
        val additionalParams = mapOf(
            "client_secret" to secretKey,
        )
        return createTokenExchangeRequest(additionalParams)
    }

    companion object {
        const val TWITCH_AUTH_STATE_KEY = "TWITCH_AUTH_STATE"
    }
}