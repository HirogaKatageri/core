package dev.hirogakatageri.sandbox.data.service

import com.haroldadmin.cnradapter.NetworkResponse
import dev.hirogakatageri.sandbox.data.model.BaseResponse
import dev.hirogakatageri.sandbox.data.payload.auth.VerifyUserPayload
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/verify")
    suspend fun verifyUser(
        @Body payload: VerifyUserPayload
    ): NetworkResponse<BaseResponse<Any>, BaseResponse<Any>>

}