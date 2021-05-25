package dev.hirogakatageri.android.sandbox.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.hirogakatageri.android.sandbox.ui.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _state: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.MainScreen())
    val state: StateFlow<ScreenState> = _state

    fun showTimeFragment() = viewModelScope.launch {
        _state.value = ScreenState.TimeScreen()
    }

    fun showOAuthFragment() = viewModelScope.launch {
        _state.value = ScreenState.OAuthScreen()
    }
}