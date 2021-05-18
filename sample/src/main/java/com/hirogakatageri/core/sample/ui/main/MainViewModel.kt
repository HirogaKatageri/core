package com.hirogakatageri.core.sample.ui.main

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hirogakatageri.core.sample.ui.ScreenState
import com.hirogakatageri.core.sample.util.Clock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val clock: Clock) : ViewModel() {

    private val _state: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Origin())
    val state: StateFlow<ScreenState> = _state

    fun showTimeFragment() = viewModelScope.launch {
        _state.value = ScreenState.TimeScreen()
    }

    fun attachClock(lifecycleOwner: LifecycleOwner) = viewModelScope.launch {
        clock.attachLifecycle(lifecycleOwner) { time ->
            _state.value = ScreenState.TimeUpdated(time = time)
        }
    }
}