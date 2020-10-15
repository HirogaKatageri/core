package com.hirogakatageri.remote.service

import com.hirogakatageri.remote.model.RemoteUserModelItem
import com.hirogakatageri.remote.wrapper.ResponseWrapper
import retrofit2.http.GET
import retrofit2.http.Query

interface MainService {

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Long
    ): ResponseWrapper<List<RemoteUserModelItem>, Any>

}