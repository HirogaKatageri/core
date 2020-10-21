package com.hirogakatageri.remote.wrapper

import androidx.annotation.Keep
import com.github.ajalt.timberkt.e
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import java.io.IOException

@Keep
sealed class ResponseWrapper<out T : Any, out U : Any> {
    /**
     * Success response with gg.nexplay.clips.remote.body
     */
    data class Success<T : Any>(val headers: Headers, val body: T) : ResponseWrapper<T, Nothing>()

    /**
     * Failure response with gg.nexplay.clips.remote.body
     */
    data class ApiError<U : Any>(val body: U, val code: Int) : ResponseWrapper<Nothing, U>()

    /**
     * Network error
     */
    data class NetworkError(val error: IOException) : ResponseWrapper<Nothing, Nothing>()

    /**
     * For example, json parsing error
     */
    data class UnknownError(val error: Throwable?) : ResponseWrapper<Nothing, Nothing>()
}

suspend inline fun <reified T1 : Any, reified T2 : Any> ResponseWrapper<T1, T2>.parse(
    noinline onError: suspend (T2?) -> Unit,
    noinline onSuccess: suspend (headers: Headers, T1) -> Unit
) = withContext(Dispatchers.IO) {
    when (this@parse) {
        is ResponseWrapper.Success<T1> -> onSuccess(headers, body)
        is ResponseWrapper.ApiError<T2> -> onError(body)
        is ResponseWrapper.NetworkError -> {
            onError(null)
            e(error) { "NetworkError" }
        }
        is ResponseWrapper.UnknownError -> {
            e(error) { "UnknownError" }
            onError(null)
        }
    }
}
