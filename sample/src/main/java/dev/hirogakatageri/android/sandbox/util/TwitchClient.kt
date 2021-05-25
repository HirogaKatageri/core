package dev.hirogakatageri.android.sandbox.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import dev.hirogakatageri.android.sample.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.openid.appauth.*
import net.openid.appauth.browser.BrowserDenyList
import net.openid.appauth.browser.Browsers
import net.openid.appauth.browser.VersionRange
import net.openid.appauth.browser.VersionedBrowserMatcher


class TwitchClient {

    private val serviceConfig: AuthorizationServiceConfiguration =
        AuthorizationServiceConfiguration(
            Uri.parse("https://id.twitch.tv/oauth2/authorize"),
            Uri.parse("https://id.twitch.tv/oauth2/token")
        )

    private var authState: AuthState = AuthState(serviceConfig)
    private var authService: AuthorizationService? = null

    suspend fun buildAuthRequestIntent(context: Context): Intent = withContext(Dispatchers.IO) {
        val builder = AuthorizationRequest.Builder(
            serviceConfig,
            BuildConfig.TWITCH_CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse("https://sandbox.android.hirogakatageri.dev/oauth2redirect/twitch")
        )

        val additionalParams = mapOf(
            "force_verify" to "true" // Enable force_verify to redirect to application without fallback page .
        )

        builder.setAdditionalParameters(additionalParams)

        val scope = "channel:manage:broadcast channel:read:stream_key"

        builder.setScope(scope)
        val authRequest: AuthorizationRequest = builder.build()

        val appAuthConfig = AppAuthConfiguration.Builder()
            .setBrowserMatcher(
                BrowserDenyList(
                    VersionedBrowserMatcher(
                        Browsers.SBrowser.PACKAGE_NAME,
                        Browsers.SBrowser.SIGNATURE_SET,
                        true,  // when this browser is used via a custom tab
                        VersionRange.atMost("5.3")
                    )
                )
            )
            .build()

        authService = AuthorizationService(context, appAuthConfig)
        authService!!.getAuthorizationRequestIntent(authRequest)
    }

    suspend fun verifyAuthRequestAndGetAccessToken(
        intent: Intent?,
        onSuccess: (accessToken: TokenResponse) -> Unit,
        onError: (ex: AuthorizationException) -> Unit
    ) = withContext(Dispatchers.IO) {
        if (intent != null) {

            if (BuildConfig.DEBUG) {
                val bundle = intent.extras
                bundle?.keySet()?.let { set ->
                    set.forEach { key ->
                        d { "Key: $key, Value: ${bundle.get(key)}" }
                    }
                }
            }

            val response = AuthorizationResponse.fromIntent(intent)
            val exception = AuthorizationException.fromIntent(intent)

            authState.update(response, exception)

            response?.let {
                d { "Authorization Response Received." }
                requestAccessToken(
                    response,
                    onSuccess,
                    onError
                )
            }

            exception?.let { ex ->
                e(ex) { "Authorization Exception" }
                onError(ex)
            }
        } else throw NullPointerException("OAuthResponse Intent is null")
    }

    private suspend inline fun requestAccessToken(
        response: AuthorizationResponse,
        crossinline onSuccess: (accessToken: TokenResponse) -> Unit,
        crossinline onError: (ex: AuthorizationException) -> Unit
    ) = withContext(Dispatchers.IO) {

        // Additional Required Parameters for Twitch.
        val additionalParams = mapOf(
            "client_secret" to BuildConfig.TWITCH_SECRET_KEY,
        )

        val tokenExchangeRequest = response.createTokenExchangeRequest(additionalParams)

        authService?.performTokenRequest(tokenExchangeRequest) { response, ex ->
            response?.let {
                d { "Token Exchange Response Received." }
                onSuccess(response)
            }
            ex?.let {
                e(ex) { "Token Exchange Exception." }
                onError(ex)
            }
        }
    }

}