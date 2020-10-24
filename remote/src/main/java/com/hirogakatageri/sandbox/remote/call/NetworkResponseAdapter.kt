package com.hirogakatageri.sandbox.remote.call

import androidx.annotation.Keep
import com.hirogakatageri.sandbox.remote.wrapper.ResponseWrapper
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

@Keep
class NetworkResponseAdapter<S : Any, E : Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, E>
) : CallAdapter<S, Call<ResponseWrapper<S, E>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<ResponseWrapper<S, E>> = NetworkResponseCall(call, errorBodyConverter)
}