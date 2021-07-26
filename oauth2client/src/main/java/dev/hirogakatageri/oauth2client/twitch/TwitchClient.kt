/*
 *    Copyright 2021 Gian Patrick Quintana
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package dev.hirogakatageri.oauth2client.twitch

import android.content.Context
import androidx.annotation.Keep
import androidx.core.content.edit
import dev.hirogakatageri.oauth2client.base.OAuth2Client
import dev.hirogakatageri.oauth2client.twitch.TwitchClient.Companion.TWITCH_AUTH_STATE_KEY
import dev.hirogakatageri.oauth2client.util.OAuthPreferences
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
class TwitchClient : OAuth2Client {

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
