package dev.hirogakatageri.sandbox.service.ui.profile

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import dev.hirogakatageri.sandbox.service.ServiceState.ProfileState
import dev.hirogakatageri.viewservice.viewmodel.ServiceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ProfileViewModel : ServiceViewModel() {

    private val _state: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState.Neutral())
    val state: StateFlow<ProfileState> = _state

    private var cameraProvider: ProcessCameraProvider? = null

    private fun neutral() = launch {
        _state.value = ProfileState.Neutral()
    }

    fun click() = launch {
        _state.value = ProfileState.Clicked()
        neutral()
    }

    fun moveView(x: Float, y: Float, width: Int, height: Int) =
        launch(Dispatchers.Default) {
            val newX = (x - (width / 2)).roundToInt()
            val newY = (y - (height / 2)).roundToInt()

            _state.value = ProfileState.Movement(x = newX, y = newY)
        }

    fun requestCameraProvider(context: Context) = launch(Dispatchers.IO) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()
                cameraProvider?.let {
                    _state.value = ProfileState.CameraProviderReady(
                        cameraProvider = it
                    )
                }
            },
            ContextCompat.getMainExecutor(context)
        )
    }

    fun unbindCamera() = launch {
        cameraProvider?.unbindAll()
    }

    override fun onDetach() {
        unbindCamera()
        super.onDetach()
    }
}
