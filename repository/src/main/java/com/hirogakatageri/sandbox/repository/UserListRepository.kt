package com.hirogakatageri.sandbox.repository

import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import com.github.ajalt.timberkt.d
import com.hirogakatageri.sandbox.local.dao.UserDao
import com.hirogakatageri.sandbox.local.model.LocalUserModel
import com.hirogakatageri.sandbox.local.model.base.IUserModel
import com.hirogakatageri.sandbox.remote.call.RemoteHelpers
import com.hirogakatageri.sandbox.remote.model.RemoteUserModel
import com.hirogakatageri.sandbox.remote.service.GithubService
import com.hirogakatageri.sandbox.remote.wrapper.parse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserListRepository(
    private val service: GithubService,
    private val dao: UserDao
) {
    private var localOffset: Long = 0
    private var remoteOffset: Long = 0

    private val userList: MutableList<IUserModel> = mutableListOf()

    val isListEmpty: Boolean get() = userList.isEmpty()

    suspend fun reset() {
        localOffset = 0
        remoteOffset = 0
        userList.clear()
    }

    suspend fun search(
        username: String,
        data: MutableLiveData<List<IUserModel>>
    ) = withContext(Dispatchers.IO) {
        if (username.isEmpty()) data.postValue(userList)
        else data.postValue(dao.searchComplex(username))
    }

    /**
     * Gets list of users from database.
     * Increases offset dependending on the size of list.
     * */
    suspend fun getLocalUsers(
        data: MutableLiveData<List<IUserModel>>
    ) = withContext(Dispatchers.IO) {
        val toAdd = dao.getUsers(localOffset)
        if (toAdd.isNotEmpty()) localOffset += toAdd.size
        userList.addAll(toAdd)
        data.postValue(userList)
    }

    suspend fun refreshCurrentList(
        data: MutableLiveData<List<IUserModel>>
    ) = withContext(Dispatchers.Default) {
        userList.forEachIndexed { index, localUserModel ->
            val latestModel = dao.getUser(localUserModel.username)
            latestModel?.let { userList[index] = latestModel }
        }
        data.postValue(userList)
    }

    /**
     * Gets list of users from remote.
     * Will update database of new list.
     * */
    suspend fun getRemoteUsers(
        onError: suspend () -> Unit,
        onSuccess: suspend () -> Unit
    ) = withContext(Dispatchers.IO) {
        service.getUsers(remoteOffset).parse(
            onError = { error -> onError() },
            onSuccess = { headers, list ->
                updateLocalUserModelListDetails(list)
                remoteOffset = RemoteHelpers.getNextOffsetOfUsers(headers)
                onSuccess().also { d { "getRemoteUsers.onSuccess" } }
            }
        )
    }

    suspend fun updateLocalUserModelListDetails(
        remoteList: List<RemoteUserModel>
    ) = withContext(Dispatchers.Default) {
        val updatedList: MutableList<LocalUserModel> = mutableListOf()

        val startTime = SystemClock.uptimeMillis()
        d { "updateLocalUserModelListDetails start..." }
        remoteList.forEach { remote ->
            dao.getUser(remote.login)?.copy(
                username = remote.login,
                profileImageUrl = remote.avatarUrl
            )?.let { local ->
                updatedList.add(local)
            } ?: updatedList.add(remote.toLocalUserModel())
        }
        dao.insertUsers(*updatedList.toTypedArray())
        d { "updateLocalUserModelListDetails finished... ${SystemClock.uptimeMillis() - startTime}ms" }
    }
}