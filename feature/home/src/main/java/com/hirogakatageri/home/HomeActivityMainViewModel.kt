package com.hirogakatageri.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.kotlin.ViewBindingHolder
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.hirogakatageri.core.viewmodel.BaseViewModel
import com.hirogakatageri.home.model.base.IMainItemUser
import com.hirogakatageri.local.model.base.IUserModel
import com.hirogakatageri.repository.UserListRepository
import com.hirogakatageri.utils.GenericEpoxyViewBindingClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivityMainViewModel(private val repository: UserListRepository) : BaseViewModel(),
    GenericEpoxyViewBindingClickListener {

    private val _userList: MutableLiveData<List<IUserModel>> = MutableLiveData()
    val userList: LiveData<List<IUserModel>> = _userList

    override suspend fun start(): Job = viewModelScope.launch(Dispatchers.IO + job) {
        getInitialLocalUsers()
        getInitialRemoteUsers()
    }

    private suspend fun getInitialLocalUsers() = withContext(Dispatchers.IO) {
        repository.getLocalUsers(_userList)
    }

    private suspend fun getInitialRemoteUsers() = withContext(Dispatchers.IO) {
        repository.getRemoteUsers(
            onError = { e { "getInitialRemoteUsers" } },
            onSuccess = {
                launch {
                    if (repository.isListEmpty) repository.getLocalUsers(_userList)
                    else repository.refreshCurrentList(_userList)
                }
            }
        )
    }

    fun search(username: String) = viewModelScope.launch(Dispatchers.IO + job) {
        repository.search(username, _userList)
    }

    fun getMoreUsers() = viewModelScope.launch(Dispatchers.IO + job) {
        repository.getRemoteUsers(
            onError = { e { "getRemoteUsers error..." } },
            onSuccess = { }
        )
    }

    override fun onClickEpoxyModel(
        model: EpoxyModel<*>,
        parentView: ViewBindingHolder,
        clickedView: View,
        position: Int
    ) {
        when (model) {
            is IMainItemUser -> {
                d { "Clicked: ${model.model.username}" }
            }
        }
    }
}