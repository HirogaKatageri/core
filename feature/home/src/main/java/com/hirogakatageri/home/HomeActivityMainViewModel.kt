package com.hirogakatageri.home

import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.kotlin.ViewBindingHolder
import com.gaelmarhic.quadrant.QuadrantConstants.PROFILE_ACTIVITY_MAIN
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.hirogakatageri.core.utils.NavigationUtil.UID
import com.hirogakatageri.core.utils.NavigationUtil.USERNAME
import com.hirogakatageri.core.utils.NetworkLiveData
import com.hirogakatageri.core.viewmodel.BaseViewModel
import com.hirogakatageri.home.model.base.IMainItemUser
import com.hirogakatageri.local.model.base.IUserModel
import com.hirogakatageri.repository.UserListRepository
import com.hirogakatageri.utils.EndlessRecyclerViewScrollListener
import com.hirogakatageri.utils.GenericEpoxyViewBindingClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivityMainViewModel(
    private val repository: UserListRepository,
    val network: NetworkLiveData
) : BaseViewModel(),
    GenericEpoxyViewBindingClickListener {

    internal fun getScrollListener(layoutManager: LinearLayoutManager) =
        HomeActivityEndlessScrollListener(layoutManager)

    private val _userList: MutableLiveData<List<IUserModel>> = MutableLiveData()
    val userList: LiveData<List<IUserModel>> = _userList

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    private var userListQueryFailed: Boolean = false

    override suspend fun start(): Job = viewModelScope.launch(Dispatchers.IO + job) {
        getInitialLocalUsers()
        getInitialRemoteUsers()
    }

    private suspend fun getInitialLocalUsers() = withContext(Dispatchers.IO) {
        repository.getLocalUsers(_userList)
    }

    private suspend fun getInitialRemoteUsers() = withContext(Dispatchers.IO) {
        _isLoading.postValue(true)
        repository.getRemoteUsers(
            onError = {
                e { "getInitialRemoteUsers error..." }
                userListQueryFailed = true
                _isLoading.postValue(false)
            },
            onSuccess = {
                userListQueryFailed = false
                if (repository.isListEmpty) repository.getLocalUsers(_userList)
                else repository.refreshCurrentList(_userList)
                _isLoading.postValue(false)
            }
        )
    }

    fun refreshCurrentList() = viewModelScope.launch(Dispatchers.IO + job) {
        repository.refreshCurrentList(_userList)
    }

    fun search(username: String) = viewModelScope.launch(Dispatchers.IO + job) {
        repository.search(username, _userList)
    }

    fun getMoreUsers() = viewModelScope.launch(Dispatchers.IO + job) {
        _isLoading.postValue(true)
        repository.getRemoteUsers(
            onError = {
                e { "getRemoteUsers error..." }
                userListQueryFailed = true
                _isLoading.postValue(false)
            },
            onSuccess = {
                userListQueryFailed = false
                repository.getLocalUsers(_userList)
                _isLoading.postValue(false)
            }
        )
    }

    fun retryQuery() {
        if (userListQueryFailed) getMoreUsers()
    }

    override fun onClickEpoxyModel(
        model: EpoxyModel<*>,
        parentView: ViewBindingHolder,
        clickedView: View,
        position: Int
    ) {
        d { "Epoxy model clicked..." }
        when (model) {
            is IMainItemUser -> {
                _toStartActivity.postValue(
                    PROFILE_ACTIVITY_MAIN to bundleOf(
                        USERNAME to model.model.username,
                        UID to model.model.uid
                    )
                )
            }
        }
    }

    internal inner class HomeActivityEndlessScrollListener(
        layoutManager: LinearLayoutManager
    ) : EndlessRecyclerViewScrollListener(layoutManager) {

        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            getMoreUsers()
        }
    }
}