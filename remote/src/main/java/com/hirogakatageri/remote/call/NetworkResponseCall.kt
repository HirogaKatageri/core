package com.hirogakatageri.remote.call

import androidx.annotation.Keep
import com.hirogakatageri.remote.wrapper.ResponseWrapper
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

@Keep
internal class NetworkResponseCall<S : Any, E : Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<ResponseWrapper<S, E>> {

    override fun enqueue(callback: Callback<ResponseWrapper<S, E>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val headers = response.headers()
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(ResponseWrapper.Success(headers, body))
                        )
                    } else {
                        // Response is successful but the gg.nexplay.clips.remote.body is null
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(ResponseWrapper.UnknownError(null))
                        )
                    }
                } else {
                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> try {
                            errorConverter.convert(error)
                        } catch (ex: Exception) {
                            null
                        }
                    }
                    if (errorBody != null) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(ResponseWrapper.ApiError(errorBody, code))
                        )
                    } else {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(ResponseWrapper.UnknownError(null))
                        )
                    }
                }
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                val ResponseWrapper = when (throwable) {
                    is IOException -> ResponseWrapper.NetworkError(throwable)
                    else -> ResponseWrapper.UnknownError(throwable)
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(ResponseWrapper))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<ResponseWrapper<S, E>> {
        throw UnsupportedOperationException("ResponseWrapperCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}