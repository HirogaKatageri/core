package com.hirogakatageri.network

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences

internal class SecuredNetworkPreferences(
    context: Context,
    fileName: String,
    masterKeyAlias: String
) {

    private val sharedPreferences: SharedPreferences =
        EncryptedSharedPreferences.create(
            fileName,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    internal var accessToken: String?
        get() = sharedPreferences.getString("ACCESS_TOKEN", null)
        set(value) {
            sharedPreferences.edit {
                putString("ACCESS_TOKEN", value)
            }
        }
}