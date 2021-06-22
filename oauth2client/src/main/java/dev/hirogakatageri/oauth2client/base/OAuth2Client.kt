package dev.hirogakatageri.oauth2client.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.Keep
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import dev.hirogakatageri.oauth2client.util.OAuthPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.openid.appauth.*

@Keep
abstract class OAuth2Client {

    constructor(
        authorizationUrl: String,
        tokenUrl: String,
        clientId: String,
        scope: String,
        preferences: OAuthPreferences
    ) {
        this.clientId = clientId
        this.scope = scope
        this.preferences = preferences

        serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse(authorizationUrl),
            Uri.parse(tokenUrl)
        )
    }

    constructor(
        context: Context,
        authorizationUrl: String,
        tokenUrl: String,
        clientId: String,
        scope: String,
        preferences: OAuthPreferences
    ) {
        this.clientId = clientId
        this.scope = scope
        this.preferences = preferences

        serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse(authorizationUrl),
            Uri.parse(tokenUrl)
        )

        initialize(context)
    }

    /**
     * Client ID used for our OAuth Session.
     * */
    protected val clientId: String

    /**
     * Scope of permissions.
     * */
    protected val scope: String

    /**
     * Where we cache our OAuth States.
     * */
    protected val preferences: OAuthPreferences

    /**
     * @see AuthState
     * */
    protected lateinit var authState: AuthState

    /**
     * @see AuthorizationService
     * */
    protected lateinit var authService: AuthorizationService

    /**
     * @see AuthorizationServiceConfiguration
     * */
    protected val serviceConfig: AuthorizationServiceConfiguration

    /**
     * @see AppAuthConfiguration
     * */
    open val appAuthConfig: AppAuthConfiguration = AppAuthConfiguration.Builder().build()

    /**
     * The default parameters to include in an Auth Request.
     * */
    open val defaultAuthRequestParams: Map<String, String> = emptyMap()

    /**
     * This is called during initialize().
     * e.g. authState = AuthState(config)
     * */
    abstract fun initializeAuthState()

    /**
     * You may call this if your are not initializing the class with a Context.
     * If you are initializing this with a Context it is not necessary to call this as this is called automatically.
     * */
    fun initialize(context: Context) {
        initializeAuthState()
        authService = AuthorizationService(context, appAuthConfig)
    }

    /**
     * @return AuthState.isAuthorized.
     * @see AuthState.isAuthorized
     * */
    val isSignedIn: Boolean get() = authState.isAuthorized

    /**
     * @return AuthState.needsTokenRefresh.
     * @see AuthState.getNeedsTokenRefresh
     * */
    val isAccessTokenExpired: Boolean get() = authState.needsTokenRefresh

    /**
     * @param redirectUri Uri used to redirect after Authorization.
     * @param responseType ("code","token","id_token")
     * @param additionalParams Additional parameters to include in Authorization Request.
     * @see ResponseTypeValues
     * */
    suspend fun buildAuthRequestIntent(
        context: Context,
        redirectUri: Uri,
        responseType: String = "code",
        additionalParams: Map<String, String> = emptyMap()
    ): Intent = withContext(Dispatchers.IO) {
        val builder = AuthorizationRequest.Builder(
            serviceConfig,
            clientId,
            responseType,
            redirectUri
        )

        val params = mutableMapOf<String, String>()

        params.putAll(defaultAuthRequestParams)

        if (additionalParams.isNotEmpty()) params.putAll(additionalParams)

        builder.setAdditionalParameters(params)
        builder.setScope(scope)

        val authRequest: AuthorizationRequest = builder.build()

        authService.getAuthorizationRequestIntent(authRequest)
    }

    /**
     * This will parse the Authorization Response received after Authentication.
     * @param intent Result from Authorization Request.
     * */
    suspend fun verifyAuthResponse(
        intent: Intent,
        onError: (ex: Exception) -> Unit,
        onSuccess: (response: AuthorizationResponse) -> Unit
    ) = withContext(Dispatchers.IO) {

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

    /**
     * Requests an Access Token for our OAuth Session.
     * @param authorizationResponse Is the response received after successful authentication.
     * @see AuthorizationResponse
     * */
    suspend fun requestAccessToken(
        authorizationResponse: AuthorizationResponse,
        onError: (ex: Exception) -> Unit,
        onSuccess: (accessToken: String?) -> Unit
    ) = withContext(Dispatchers.IO) {

        val request = authorizationResponse.buildTokenExchangeRequest()

        authService.performTokenRequest(request) { response, error ->
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

    /**
     * Requests an Access Token for our OAuth Session.
     * @param onAuthRequired Requires the user to re-authenticate.
     * */
    suspend fun requestAccessToken(
        onAuthRequired: () -> Unit,
        onError: (ex: Exception) -> Unit,
        onSuccess: (accessToken: String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        val isAccessTokenExpired = authState.needsTokenRefresh
        val isAuthTokenExpired =
            authState.lastAuthorizationResponse?.hasAccessTokenExpired() == true

        // Check if Both Access Token and Auth Token are expired.
        // If true user must re-authenticate.
        if (isAccessTokenExpired && isAuthTokenExpired) onAuthRequired()

        // If only access token is expired.
        // Request new access token.
        if (isAccessTokenExpired && authState.lastAuthorizationResponse != null) {
            val request = authState.lastAuthorizationResponse!!.buildTokenExchangeRequest()
            authService.performTokenRequest(request) { response, error ->
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
        } else onSuccess(authState.accessToken)
    }

    /**
     * Called after updateAuthState().
     * */
    open fun onAuthStateUpdated(newState: AuthState) {}

    protected suspend fun updateAuthState(
        response: AuthorizationResponse?,
        ex: AuthorizationException?
    ) = withContext(Dispatchers.IO) {
        authState.update(response, ex)
        onAuthStateUpdated(authState)
    }

    protected suspend fun updateAuthState(
        response: TokenResponse?,
        ex: AuthorizationException?
    ) = withContext(Dispatchers.IO) {
        authState.update(response, ex)
        onAuthStateUpdated(authState)
    }

    protected suspend fun updateAuthState(
        response: RegistrationResponse?
    ) = withContext(Dispatchers.IO) {
        authState.update(response)
        onAuthStateUpdated(authState)
    }

    /**
     * Can be overriden to add and include other parameters.
     * e.g.
     * val additionalParams = mapOf("secret_key" to "my_secret_key")
     * return createTokenExchangeRequest(additionalParams)
     * */
    protected open fun AuthorizationResponse.buildTokenExchangeRequest(): TokenRequest {
        return createTokenExchangeRequest()
    }

    /**
     * Destroys our client session.
     * */
    suspend fun signOut() = withContext(Dispatchers.IO) {
        val newState = AuthState(serviceConfig)
        authState = newState
        onAuthStateUpdated(newState)
    }

    /**
     * Call this when the client will no longer be used to avoid memory leaks.
     * */
    fun dispose() {
        authService.dispose()
    }

}