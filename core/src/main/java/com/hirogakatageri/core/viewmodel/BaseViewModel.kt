package com.hirogakatageri.core.viewmodel

import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Keep
abstract class BaseViewModel : ViewModel() {

    open suspend fun start(): Job = CoroutineScope(Dispatchers.IO).launch {}

    protected fun <T> get(liveData: MutableLiveData<T>, default: T) = viewModelScope.launch {
        if (liveData.value == null) liveData.postValue(default)
    }
}