package com.hirogakatageri.base.viewmodel

import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Keep
abstract class BaseViewModel : ViewModel() {

    abstract suspend fun start(): Job

    protected fun <T> get(liveData: MutableLiveData<T>, default: T) = viewModelScope.launch {
        if (liveData.value == null) liveData.postValue(default)
    }
}