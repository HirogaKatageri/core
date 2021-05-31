package dev.hirogakatageri.android.sandbox.service.ui.profile

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import dev.hirogakatageri.android.sandbox.service.ServiceEvent
import dev.hirogakatageri.android.sandbox.service.ServiceEvent.ProfileEvent
import dev.hirogakatageri.android.sandbox.service.ServiceState.ProfileState
import dev.hirogakatageri.android.sandbox.service.components.ServiceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ProfileViewModel : ServiceViewModel() {

    private val _state: MutableStateFlow<ProfileState> =
        MutableStateFlow(ProfileState.Neutral())
    val state: StateFlow<ProfileState> = _state

    override fun notifyEvent(event: ServiceEvent) {
        when (event) {
            is ProfileEvent.ProfileMoved -> mapMovement(event)
            is ProfileEvent.ProfileClicked -> mapClick(event)
        }
    }

    private fun mapNeutral() = launch {
        _state.value = ProfileState.Neutral()
    }

    private fun mapMovement(event: ProfileEvent.ProfileMoved) = launch(Dispatchers.Default) {
        val newX = (event.x - (event.width / 2)).roundToInt()
        val newY = (event.y - (event.height / 2)).roundToInt()

        _state.value = ProfileState.Movement(x = newX, y = newY)
    }

    private fun mapClick(event: ProfileEvent.ProfileClicked) = launch {
        _state.value = ProfileState.Clicked()
        mapNeutral()
    }

    private fun mapCameraProviderReady(cameraProvider: ProcessCameraProvider) = launch {
        _state.value = ProfileState.CameraProviderReady(cameraProvider = cameraProvider)
        mapNeutral()
    }

    fun requestCameraProvider(context: Context) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener(
            {
                val cameraProvider = cameraProviderFuture.get()
                mapCameraProviderReady(cameraProvider)
            }, ContextCompat.getMainExecutor(context)
        )
    }
}