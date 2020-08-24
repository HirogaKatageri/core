package com.hirogakatageri.sandbox

import androidx.lifecycle.viewModelScope
import com.hirogakatageri.core.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {

    override suspend fun start(): Job = viewModelScope.launch {

    }
}