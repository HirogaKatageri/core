package dev.hirogakatageri.android.sandbox.api

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import dev.hirogakatageri.android.sample.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.openid.appauth.*
import net.openid.appauth.browser.BrowserDenyList
import net.openid.appauth.browser.Browsers
import net.openid.appauth.browser.VersionRange
import net.openid.appauth.browser.VersionedBrowserMatcher
import java.lang.ref.WeakReference


class TwitchClient {

    constructor(
        preferences: OAuthPreferences
    ) : super() {
        _oAuthPreferences = preferences
    }

    constructor(
        context: Context,
        preferences: OAuthPreferences
    ) : super() {
        _oAuthPreferences = preferences
        initialize(context)
    }

    private val _oAuthPreferences: OAuthPreferences

    private var _context: WeakReference<Context> = WeakReference(null)

    private lateinit var _authState: AuthState
    private lateinit var _authService: AuthorizationService

    private val _serviceConfig: AuthorizationServiceConfiguration =
        AuthorizationServiceConfiguration(
            Uri.parse("https://id.twitch.tv/oauth2/authorize"),
            Uri.parse("https://id.twitch.tv/oauth2/token")
        )

    private val _appAuthConfig = AppAuthConfiguration.Builder()
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


    fun initialize(context: Context) {
        _context = WeakReference(context)
        _authState = _oAuthPreferences.getCachedTwitchState(_serviceConfig)
        _authService = AuthorizationService(context, _appAuthConfig)
    }

    val isSignedIn: Boolean get() = _authState.isAuthorized

    suspend fun buildAuthRequestIntent(
        context: Context,
        redirectUri: Uri = Uri.parse("https://sandbox.android.hirogakatageri.dev/oauth2redirect/twitch")
    ): Intent = withContext(Dispatchers.IO) {
        val builder = AuthorizationRequest.Builder(
            _serviceConfig,
            BuildConfig.TWITCH_CLIENT_ID,
            ResponseTypeValues.CODE,
            redirectUri
        )

        val additionalParams = mapOf(
            "force_verify" to "true" // Enable force_verify to redirect to application without fallback page .
        )

        builder.setAdditionalParameters(additionalParams)

        val scope = "channel:manage:broadcast channel:read:stream_key"

        builder.setScope(scope)
        val authRequest: AuthorizationRequest = builder.build()

        _authService.getAuthorizationRequestIntent(authRequest)
    }

    suspend fun verifyAuthRequest(
        intent: Intent,
        onError: suspend (ex: Exception) -> Unit,
        onSuccess: suspend (response: AuthorizationResponse) -> Unit
    ) = withContext(Dispatchers.IO) {
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

        try {
            updateAuthState(response, exception)
        } catch (ex: Exception) {
            onError(ex)
        }

        response?.let {
            d { "Authorization Response Received" }
            onSuccess(response)
        }

        exception?.let { ex ->
            e(ex) { "Authorization Exception" }
            onError(ex)
        }
    }

    suspend fun requestAccessToken(
        authorizationResponse: AuthorizationResponse,
        onError: (ex: Exception) -> Unit,
        onSuccess: (accessToken: String?) -> Unit
    ) = withContext(Dispatchers.IO) {

        val request = authorizationResponse.buildTokenExchangeRequest()

        _authService.performTokenRequest(request) { response, error ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    updateAuthState(response, error)
                } catch (ex: Exception) {
                    onError(ex)
                }
            }
            response?.let {
                d { "Token Exchange Response Received" }
                onSuccess(response.accessToken)
            }
            error?.let {
                e(error) { "Token Exchange Exception" }
                onError(error)
            }
        }
    }

    suspend fun requestAccessToken(
        onAuthRequired: () -> Unit,
        onError: (ex: Exception) -> Unit,
        onSuccess: (accessToken: String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        val isAccessTokenExpired = _authState.needsTokenRefresh
        val isAuthTokenExpired =
            _authState.lastAuthorizationResponse?.hasAccessTokenExpired() == true

        // Check if Both Access Token and Auth Token are expired.
        // If true user must re-authenticate.
        if (isAccessTokenExpired && isAuthTokenExpired) onAuthRequired()

        // If only access token is expired.
        // Request new access token.
        if (isAccessTokenExpired && _authState.lastAuthorizationResponse != null) {
            val request = _authState.lastAuthorizationResponse!!.buildTokenExchangeRequest()
            _authService.performTokenRequest(request) { response, error ->
                response?.let {
                    d { "Token Exchange Response Received" }
                    onSuccess(response.accessToken)
                }
                error?.let {
                    e(error) { "Token Exchange Exception" }
                    onError(error)
                }
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        updateAuthState(response, error)
                    } catch (ex: Exception) {
                        onError(ex)
                    }
                }
            }
        } else onSuccess(_authState.accessToken)
    }

    private suspend inline fun updateAuthState(
        response: AuthorizationResponse?,
        ex: AuthorizationException?
    ) = withContext(Dispatchers.IO) {
        _authState.update(response, ex)
        _oAuthPreferences.updateCachedTwitchState(_authState)
    }

    private suspend inline fun updateAuthState(
        response: TokenResponse?,
        ex: AuthorizationException?
    ) = withContext(Dispatchers.IO) {
        _authState.update(response, ex)
        _oAuthPreferences.updateCachedTwitchState(_authState)
    }

    private suspend inline fun updateAuthState(
        response: RegistrationResponse?
    ) = withContext(Dispatchers.IO) {
        _authState.update(response)
        _oAuthPreferences.updateCachedTwitchState(_authState)
    }

    private fun AuthorizationResponse.buildTokenExchangeRequest(): TokenRequest {
        // Additional Required Parameters for Twitch.
        val additionalParams = mapOf(
            "client_secret" to BuildConfig.TWITCH_SECRET_KEY,
        )
        return createTokenExchangeRequest(additionalParams)
    }

    suspend fun signOut() = withContext(Dispatchers.IO) {
        val newState = AuthState(_serviceConfig)
        _authState = newState
        _oAuthPreferences.updateCachedTwitchState(newState)
    }

}