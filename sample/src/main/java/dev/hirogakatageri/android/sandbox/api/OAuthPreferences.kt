package dev.hirogakatageri.android.sandbox.api

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationServiceConfiguration
import java.lang.ref.WeakReference

class OAuthPreferences(private val context: WeakReference<Context>) {

    private val _mainKey: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val _preferences: SharedPreferences = EncryptedSharedPreferences.create(
        FILE_NAME,
        _mainKey,
        context.get()
            ?: throw RuntimeException("Failed to create EncryptedSharedPreferences, Context Reference is missing."),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getCachedTwitchState(config: AuthorizationServiceConfiguration): AuthState =
        _preferences.getString(TWITCH_AUTH_STATE, null)?.let { stateJson ->
            AuthState.jsonDeserialize(stateJson)
        } ?: AuthState(config)

    fun updateCachedTwitchState(state: AuthState) {
        _preferences.edit {
            putString(TWITCH_AUTH_STATE, state.jsonSerializeString())
        }
    }

    companion object {
        private const val FILE_NAME = "OAUTH_PREFERENCES"
        private const val TWITCH_AUTH_STATE = "TWITCH_AUTH_STATE"
    }

}