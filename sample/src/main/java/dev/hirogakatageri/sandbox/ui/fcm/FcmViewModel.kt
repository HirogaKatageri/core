package dev.hirogakatageri.sandbox.ui.fcm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FcmViewModel : ViewModel() {

    private val _state: MutableStateFlow<FcmFragmentState> =
        MutableStateFlow(FcmFragmentState.Neutral())
    val state: StateFlow<FcmFragmentState> = _state

    fun setArgs(args: FcmFragmentArgs) {
        if (args.message.isBlank())
            _state.value = FcmFragmentState.Neutral()
        else
            _state.value = FcmFragmentState.NotificationReceived(message = args.message)
    }
}
