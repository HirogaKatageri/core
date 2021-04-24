package com.hirogakatageri.core.sample.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hirogakatageri.core.sample.ui.UiState
import com.hirogakatageri.core.sample.util.Clock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val clock: Clock) : ViewModel() {

    private val _state: MutableStateFlow<UiState> = MutableStateFlow(UiState.Created())
    val state: StateFlow<UiState> = _state

    fun showTimeFragment() = viewModelScope.launch {
        _state.value = UiState.TimeFragmentStarting()
    }

    fun startTimer() = viewModelScope.launch {
        clock.start { time -> _state.value = UiState.TimeUpdated(time) }
    }

    fun stopTimer() = viewModelScope.launch(Dispatchers.IO) {
        clock.stop()
    }

}