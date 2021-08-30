package dev.hirogakatageri.sandbox.ui.time

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.hirogakatageri.sandbox.util.Clock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TimeViewModel(private val clock: Clock) : ViewModel() {

    private val _state: MutableStateFlow<TimeFragmentState> =
        MutableStateFlow(TimeFragmentState.Origin())
    val state: StateFlow<TimeFragmentState> = _state

    fun attachClock(lifecycleOwner: LifecycleOwner) = viewModelScope.launch {
        clock.attachLifecycle(lifecycleOwner) { time ->
            _state.value = TimeFragmentState.TimeUpdated(time = time)
        }
    }
}
