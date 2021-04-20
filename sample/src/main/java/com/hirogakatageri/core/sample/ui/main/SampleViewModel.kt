package com.hirogakatageri.core.sample.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hirogakatageri.core.sample.util.Clock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SampleViewModel(private val clock: Clock) : ViewModel() {

    private val _state: MutableStateFlow<SampleState> = MutableStateFlow(SampleState.Created())
    val state: StateFlow<SampleState> = _state

    fun startTimer() = viewModelScope.launch {
        clock.start { time -> _state.value = SampleState.TimeUpdated(time) }
    }

    fun stopTimer() = viewModelScope.launch(Dispatchers.IO) {
        clock.stop()
    }

}