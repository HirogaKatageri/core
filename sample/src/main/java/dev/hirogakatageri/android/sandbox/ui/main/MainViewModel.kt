package dev.hirogakatageri.android.sandbox.ui.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.hirogakatageri.android.sandbox.ui.ScreenState
import dev.hirogakatageri.android.sandbox.util.Clock
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