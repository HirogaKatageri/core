package com.hirogakatageri.repository

import com.hirogakatageri.local.dao.UserDao
import com.hirogakatageri.local.model.LocalUserModel
import com.hirogakatageri.remote.model.RemoteUserModel
import com.hirogakatageri.remote.service.MainService
import com.hirogakatageri.remote.wrapper.parse

class UserRepository(
    val service: MainService,
    val dao: UserDao
) {
    private var offset: Long = 0

    suspend fun getLocalUsers() = dao.getUsers(offset)

    suspend fun getRemoteUsers(
        onError: () -> Unit,
        onSuccess: (list: List<LocalUserModel>) -> Unit
    ) {
        service.getUsers(offset).parse(
            onError = { error -> onError() },
            onSuccess = { headers, list ->
                val newList: MutableList<LocalUserModel> = mutableListOf()
                list.forEach { remote -> remote.toLocalUserModel()?.let { local -> newList.add(local) } }
                onSuccess(newList)
            }
        )
    }

    suspend fun updateLocalUserModelListDetails(remoteList: List<RemoteUserModel>) {
        val updatedList: MutableList<LocalUserModel> = mutableListOf()

        remoteList.forEach { remote ->
            dao.getUser(remote.id)?.copy(
                username = remote.login,
                profileImageUrl = remote.avatarUrl
            )?.let { updatedList.add(it) }
        }

        dao.insertUsers(*updatedList.toTypedArray())
    }

    suspend fun updateLocalUserModelDetails(remoteModel: RemoteUserModel) {
        val updatedModel = dao.getUser(remoteModel.id).copy(
            username = remoteModel.login,
            profileImageUrl = remoteModel.avatarUrl,
            followers = remoteModel.followers ?: 0,
            followings = remoteModel.following ?: 0,
            companyName = remoteModel.company,
            name = remoteModel.name,
            blogUrl = remoteModel.blog
        )

        dao.updateUsers(updatedModel)
    }

}