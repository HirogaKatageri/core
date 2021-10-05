package dev.hirogakatageri.sandbox.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination
import dev.hirogakatageri.sandbox.R
import dev.hirogakatageri.sandbox.ui.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ParentViewModel : ViewModel() {

    private val _state: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.MainScreen())
    val state: StateFlow<ScreenState> = _state

    fun onNavigationDestinationChanged(destination: NavDestination) {
        if (destination.id == R.id.main_fragment) {
            _state.value = ScreenState.MainScreen()
        }
    }

    fun showTimeFragment() = viewModelScope.launch {
        _state.value = ScreenState.TimeScreen()
    }

    fun showOAuthFragment() = viewModelScope.launch {
        _state.value = ScreenState.OAuthScreen()
    }

    fun showFcmFragment(message: String = "") = viewModelScope.launch {
        _state.value = ScreenState.FCMScreen(message = message)
    }

    fun showChatFragment() = viewModelScope.launch {
        _state.value = ScreenState.ChatScreen()
    }
}
