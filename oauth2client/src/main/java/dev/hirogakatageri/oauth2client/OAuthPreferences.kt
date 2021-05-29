package dev.hirogakatageri.oauth2client

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.Keep
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

@Keep
class OAuthPreferences(context: Context) {

    private val mainKey: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    val preferences: SharedPreferences = EncryptedSharedPreferences.create(
        FILE_NAME,
        mainKey,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val FILE_NAME = "OAUTH_PREFERENCES"
    }

}