package com.hirogakatageri.remote.service

import com.hirogakatageri.remote.model.RemoteUserModel
import com.hirogakatageri.remote.model.RemoteUserModelItem
import com.hirogakatageri.remote.wrapper.ResponseWrapper
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MainService {

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Long,
        @Query("per_page") perPage: Int = 20
    ): ResponseWrapper<List<RemoteUserModelItem>, Any>

    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): ResponseWrapper<RemoteUserModel, Any>

}