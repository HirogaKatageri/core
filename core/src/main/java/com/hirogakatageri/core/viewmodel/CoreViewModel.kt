package com.hirogakatageri.core.viewmodel

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Keep
abstract class CoreViewModel : ViewModel() {

    protected val job = SupervisorJob()

    open suspend fun start(): Job = viewModelScope.launch(Dispatchers.IO + job) {}

}