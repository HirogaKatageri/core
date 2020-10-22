package com.hirogakatageri.core.viewmodel

import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hirogakatageri.core.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Keep
abstract class BaseViewModel : ViewModel() {

    protected val job = SupervisorJob()

    protected val _toStartActivity: SingleLiveEvent<String> = SingleLiveEvent()
    val toStartActivity: LiveData<String> = _toStartActivity

    open suspend fun start(): Job = viewModelScope.launch(Dispatchers.IO + job) {}

}