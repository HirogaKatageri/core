package dev.hirogakatageri.sandbox.data.payload.auth

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VerifyUserPayload(
    val idToken: String?
)