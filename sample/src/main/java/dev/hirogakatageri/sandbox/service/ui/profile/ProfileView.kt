package dev.hirogakatageri.sandbox.service.ui.profile

import android.content.res.Configuration
import android.graphics.PixelFormat
import android.os.Build
import android.util.Size
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager.LayoutParams
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView.ImplementationMode
import com.github.ajalt.timberkt.d
import dev.hirogakatageri.sandbox.databinding.ServiceViewProfileBinding
import dev.hirogakatageri.sandbox.service.ServiceState.ProfileState
import dev.hirogakatageri.sandbox.service.ServiceStateModel
import dev.hirogakatageri.viewservice.util.LifecycleServiceProvider
import dev.hirogakatageri.viewservice.view.ViewBindingViewModelServiceView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileView(
    serviceProvider: LifecycleServiceProvider,
    themeResId: Int,
    viewModel: ProfileViewModel,
    val serviceModel: ServiceStateModel
) : ViewBindingViewModelServiceView<ServiceViewProfileBinding, ProfileViewModel>(
    serviceProvider,
    themeResId,
    viewModel
) {

    private val onTouchListener = OnTouchProfileView(
        onMove = { x, y ->
            viewModel.moveView(x, y, binding.root.width, binding.root.height)
        }
    )

    private val onClickListener = View.OnClickListener {
        viewModel.click()
    }

    override fun createView() {
        binding = ServiceViewProfileBinding.inflate(layoutInflater)
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT < 26) LayoutParams.TYPE_PHONE else LayoutParams.TYPE_APPLICATION_OVERLAY,
            LayoutParams.FLAG_NOT_FOCUSABLE or LayoutParams.FLAG_NOT_TOUCH_MODAL or LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).also { params ->
            params.gravity = Gravity.TOP or Gravity.START
            val x = windowRect.right
            val y = (windowRect.bottom / 100) * 70
            params.x = x
            params.y = y
        }

        binding.root.setOnTouchListener(onTouchListener)
        binding.root.setOnClickListener(onClickListener)

        launch { observeState() }
        launch { viewModel.requestCameraProvider(serviceProvider.service) }
    }

    private suspend fun observeState() = withContext(Dispatchers.Main) {
        viewModel.state.collect { state ->
            d { "State: ${state::class.simpleName}" }
            when (state) {
                is ProfileState.Movement -> onProfileMovement(state.x, state.y)
                is ProfileState.Clicked -> onProfileClicked()
                is ProfileState.CameraProviderReady -> onCameraProviderReady(state.cameraProvider)
                else -> Unit
            }
        }
    }

    private suspend fun onProfileMovement(x: Int, y: Int) = withContext(Dispatchers.Main) {
        layoutParams.x = x
        layoutParams.y = y

        windowManager.updateViewLayout(binding.root, layoutParams)
    }

    private suspend fun onProfileClicked() = withContext(Dispatchers.Main) {
        d { "Profile is Clicked." }
    }

    private suspend fun onCameraProviderReady(cameraProvider: ProcessCameraProvider) =
        withContext(Dispatchers.Main) {
            // Set Implementation Mode to Compatible in order to display preview in a TextureView.
            // This allows the Display to be shaped depending on the container.
            binding.cameraPreview.implementationMode = ImplementationMode.COMPATIBLE

            val preview = Preview.Builder()
                .setTargetResolution(Size(720, 720))
                .build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build()

            preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)

            cameraProvider.bindToLifecycle(
                serviceProvider.service,
                cameraSelector,
                preview
            )
        }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        launch {
            // Dispose Camera of camera resources due to screen rotation.
            // Request Camera Provider in order to restart Camera Preview.
            viewModel.unbindCamera()
            viewModel.requestCameraProvider(serviceProvider.service)
        }
    }

    private class OnTouchProfileView(
        private val onMove: (x: Float, y: Float) -> Unit
    ) : View.OnTouchListener {

        private val movementThreshold: Float = 25f
        private var isMovementEnabled: Boolean = false
        private var hasMoved: Boolean = false

        private var originX: Float = 0.0f
        private var originY: Float = 0.0f

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Get Original Position from Event.
                    originX = event.rawX
                    originY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    // Start movement if it has passed threshold.
                    when {
                        originX - event.rawX > movementThreshold -> isMovementEnabled = true
                        originX - event.rawX < -movementThreshold -> isMovementEnabled = true
                        originY - event.rawY > movementThreshold -> isMovementEnabled = true
                        originY - event.rawY < -movementThreshold -> isMovementEnabled = true
                    }

                    if (isMovementEnabled) {
                        hasMoved = true
                        onMove(event.rawX, event.rawY)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    // If view wasn't moved, perform click.
                    if (!hasMoved) v?.performClick()

                    hasMoved = false
                    isMovementEnabled = false
                }
            }
            return true
        }
    }
}
