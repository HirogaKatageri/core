package com.hirogakatageri.sandbox.remote.service

import com.hirogakatageri.sandbox.remote.model.RemoteUserModel
import com.hirogakatageri.sandbox.remote.wrapper.ResponseWrapper
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Long,
        @Query("per_page") limit: Int = 20
    ): ResponseWrapper<List<RemoteUserModel>, Any>

    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): ResponseWrapper<RemoteUserModel, Any>

}