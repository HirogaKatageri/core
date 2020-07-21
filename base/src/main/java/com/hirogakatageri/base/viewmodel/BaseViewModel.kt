package com.hirogakatageri.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseViewModel() : ViewModel() {

    abstract suspend fun start()

    protected suspend inline fun <T> get(liveData: MutableLiveData<T>, default: T) =
        withContext(Dispatchers.IO) {
            if (liveData.value == null) liveData.postValue(default)
        }

}