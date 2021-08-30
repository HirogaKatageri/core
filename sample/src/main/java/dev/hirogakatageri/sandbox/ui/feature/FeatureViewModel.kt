package dev.hirogakatageri.sandbox.ui.feature

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.hirogakatageri.sandbox.PermissionLauncher
import dev.hirogakatageri.sandbox.service.SampleViewService
import dev.hirogakatageri.sandbox.ui.PermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeatureViewModel(
    private val savedState: SavedStateHandle,
    private val serviceViewPermissionLauncher: PermissionLauncher
) : ViewModel() {

    private val _permissionState: MutableStateFlow<PermissionState> =
        MutableStateFlow(PermissionState.Neutral())
    val permissionState: StateFlow<PermissionState> = _permissionState

    /**
     * Saved Instance State of Feature List RecyclerView this is saved and retrieved from the
     * SavedStateHandle of the ViewModel.
     * */
    var featureListState: Parcelable?
        get() = savedState[FEATURE_LIST_STATE]
        set(value) {
            savedState[FEATURE_LIST_STATE] = value
        }

    fun resetPermissionState() {
        _permissionState.value = PermissionState.Neutral()
    }

    fun startServiceView() = viewModelScope.launch {
        requestServiceViewPermissions()
    }

    private suspend fun requestServiceViewPermissions() = withContext(Dispatchers.Main) {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
        serviceViewPermissionLauncher.launch(permissions)
    }

    fun verifyServiceViewPermissions(
        map: Map<String, Boolean>
    ) = viewModelScope.launch {
        if (map.containsValue(false))
            _permissionState.value = PermissionState.ServiceViewPermissionsDenied()
        else
            _permissionState.value = PermissionState.ServiceViewPermissionsGranted()
    }

    fun requestOverlayPermission(
        launcher: ActivityResultLauncher<Intent>,
        packageName: String
    ) = viewModelScope.launch {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        launcher.launch(intent)
    }

    fun checkOverlayPermission(isOverlayPermissionGranted: Boolean) = viewModelScope.launch {
        if (isOverlayPermissionGranted)
            _permissionState.value = PermissionState.OverlayPermissionGranted()
        else
            _permissionState.value = PermissionState.OverlayPermissionDenied()
    }

    fun startViewService(activity: Activity) {
        SampleViewService.launch(activity)
    }

    companion object {
        const val FEATURE_LIST_STATE = "FEATURE_LIST_STATE"
    }
}
