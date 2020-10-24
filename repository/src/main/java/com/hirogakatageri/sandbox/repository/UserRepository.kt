package com.hirogakatageri.sandbox.repository

import androidx.lifecycle.MutableLiveData
import com.hirogakatageri.sandbox.local.dao.UserDao
import com.hirogakatageri.sandbox.local.model.LocalUserModel
import com.hirogakatageri.sandbox.remote.model.RemoteUserModel
import com.hirogakatageri.sandbox.remote.service.GithubService
import com.hirogakatageri.sandbox.remote.wrapper.parse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val service: GithubService,
    private val dao: UserDao
) {

    private var localUserModel: LocalUserModel? = null

    suspend fun getLocalUser(
        data: MutableLiveData<LocalUserModel?>,
        username: String
    ): Unit = withContext(Dispatchers.IO) {
        localUserModel = dao.getUser(username)
        data.postValue(localUserModel)
    }

    suspend fun getRemoteUser(
        username: String,
        onError: suspend () -> Unit,
        onSuccess: suspend (model: RemoteUserModel) -> Unit
    ) = withContext(Dispatchers.IO) {
        service.getUser(username).parse(
            onError = { onError() },
            onSuccess = { _, model -> onSuccess(model) }
        )
    }

    suspend fun updateLocalUserModelDetails(
        data: MutableLiveData<LocalUserModel?>,
        remoteModel: RemoteUserModel
    ) = withContext(Dispatchers.Default) {
        localUserModel = dao.getUser(remoteModel.login)?.copy(
            username = remoteModel.login,
            profileImageUrl = remoteModel.avatarUrl,
            followers = remoteModel.followers ?: 0,
            followings = remoteModel.following ?: 0,
            companyName = remoteModel.company,
            name = remoteModel.name,
            blogUrl = remoteModel.blog
        )

        localUserModel?.let { dao.updateUsers(it) }
        data.postValue(localUserModel)
    }

    suspend fun updateNotes(
        data: MutableLiveData<Boolean>,
        notes: String
    ) = withContext(Dispatchers.Default) {
        localUserModel?.copy(notes = notes)?.let {
            dao.updateUsers(it)
            data.postValue(true)
        }
    }

}