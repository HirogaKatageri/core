package com.hirogakatageri.repository

import com.hirogakatageri.local.dao.UserDao
import com.hirogakatageri.local.model.LocalUserModel
import com.hirogakatageri.remote.model.RemoteUserModel
import com.hirogakatageri.remote.service.MainService
import com.hirogakatageri.remote.wrapper.parse

class UserRepository(
    private val service: MainService,
    private val dao: UserDao
) {

    suspend fun getLocalUser(username: String): LocalUserModel? = dao.getUser(username)

    suspend fun getRemoteUser(
        username: String,
        onError: () -> Unit,
        onSuccess: (model: RemoteUserModel) -> Unit
    ) {
        service.getUser(username).parse(
            onError = { onError() },
            onSuccess = { _, model -> onSuccess(model) }
        )
    }

    suspend fun updateLocalUserModelDetails(remoteModel: RemoteUserModel) {
        val updatedModel = dao.getUser(remoteModel.login)?.copy(
            username = remoteModel.login,
            profileImageUrl = remoteModel.avatarUrl,
            followers = remoteModel.followers ?: 0,
            followings = remoteModel.following ?: 0,
            companyName = remoteModel.company,
            name = remoteModel.name,
            blogUrl = remoteModel.blog
        )

        updatedModel?.let { dao.updateUsers(it) }
    }

}