package com.hirogakatageri.core.viewmodel

import android.os.Bundle
import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hirogakatageri.core.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Keep
abstract class BaseViewModel : ViewModel() {

    protected val job = SupervisorJob()

    protected val _toStartActivity: SingleLiveEvent<Pair<String, Bundle>> = SingleLiveEvent()
    val toStartActivity: LiveData<Pair<String, Bundle>> = _toStartActivity

    open suspend fun start(): Job = viewModelScope.launch(Dispatchers.IO + job) {}

}