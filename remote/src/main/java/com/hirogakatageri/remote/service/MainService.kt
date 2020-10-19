package com.hirogakatageri.remote.service

import com.hirogakatageri.remote.model.RemoteUserModel
import com.hirogakatageri.remote.wrapper.ResponseWrapper
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MainService {

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Long
    ): ResponseWrapper<List<RemoteUserModel>, Any>

    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): ResponseWrapper<RemoteUserModel, Any>

}