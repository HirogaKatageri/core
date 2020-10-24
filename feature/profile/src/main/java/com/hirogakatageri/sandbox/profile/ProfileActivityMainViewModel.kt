package com.hirogakatageri.sandbox.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.e
import com.hirogakatageri.core.utils.livedata.NetworkLiveData
import com.hirogakatageri.core.utils.livedata.SingleLiveEvent
import com.hirogakatageri.core.viewmodel.BaseViewModel
import com.hirogakatageri.sandbox.local.model.LocalUserModel
import com.hirogakatageri.sandbox.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivityMainViewModel(
    private val repository: UserRepository,
    val network: NetworkLiveData
) : BaseViewModel() {

    lateinit var username: String
    var uid: Int = -1

    private val _isNoteUpdateFinished: SingleLiveEvent<Boolean> =
        SingleLiveEvent()
    val isNoteUpdatedFinished: LiveData<Boolean> = _isNoteUpdateFinished

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userModel: MutableLiveData<LocalUserModel?> = MutableLiveData()
    val userModel: LiveData<LocalUserModel?> = _userModel

    private var userQueryFailed: Boolean = false

    override suspend fun start(): Job = viewModelScope.launch(Dispatchers.IO + job) {
        getUserDetails()
    }

    private suspend fun getUserDetails() = withContext(Dispatchers.IO) {
        // Retrieve local user details first then remote...
        repository.getLocalUser(_userModel, username)

        _isLoading.postValue(true)
        repository.getRemoteUser(
            username,
            onError = {
                e { "getRemoteUser error..." }
                userQueryFailed = true
                _isLoading.postValue(false)
            },
            onSuccess = { remote ->
                userQueryFailed = false
                _isLoading.postValue(false)
                repository.updateLocalUserModelDetails(_userModel, remote)
            }
        )
    }

    fun updateNotes(
        notes: String
    ) = viewModelScope.launch(Dispatchers.Default) {
        repository.updateNotes(_isNoteUpdateFinished, notes)
    }

    fun retryQuery() {
        if (userQueryFailed) viewModelScope.launch(Dispatchers.IO + job) {
            getUserDetails()
        }
    }

}