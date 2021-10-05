package dev.hirogakatageri.sandbox.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponse<T>(
    val status: Int,
    val data: T?,
    val message: String
)
