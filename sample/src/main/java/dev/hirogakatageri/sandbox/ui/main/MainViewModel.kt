package dev.hirogakatageri.sandbox.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.hirogakatageri.sandbox.module.PermissionLauncher
import dev.hirogakatageri.sandbox.service.SampleViewService
import dev.hirogakatageri.sandbox.ui.PermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val serviceViewPermissionLauncher: PermissionLauncher
) : ViewModel() {

    private val _permissionState: MutableStateFlow<PermissionState> =
        MutableStateFlow(PermissionState.Neutral())
    val permissionState: StateFlow<PermissionState> = _permissionState

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

    fun verifyPermissionServiceViewPermissions(
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
}
