package dev.hirogakatageri.android.sandbox.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.hirogakatageri.android.sandbox.service.ViewService
import dev.hirogakatageri.android.sandbox.ui.PermissionState
import dev.hirogakatageri.android.sandbox.ui.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _state: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.MainScreen())
    val state: StateFlow<ScreenState> = _state

    private val _permissionState: MutableStateFlow<PermissionState> =
        MutableStateFlow(PermissionState.Neutral())
    val permissionState: StateFlow<PermissionState> = _permissionState

    fun showTimeFragment() = viewModelScope.launch {
        _state.value = ScreenState.TimeScreen()
    }

    fun showOAuthFragment() = viewModelScope.launch {
        _state.value = ScreenState.OAuthScreen()
    }

    fun resetPermissionState() {
        _permissionState.value = PermissionState.Neutral()
    }

    fun requestServiceViewPermissions(
        launcher: ActivityResultLauncher<Array<out String>>
    ) {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
        launcher.launch(permissions)
    }

    fun verifyPermissionServiceViewPermissions(
        map: Map<String, Boolean>
    ) {
        if (map.containsValue(false)) _permissionState.value =
            PermissionState.ServiceViewPermissionsDenied()
        else _permissionState.value =
            PermissionState.ServiceViewPermissionsGranted()
    }

    fun requestOverlayPermission(
        launcher: ActivityResultLauncher<Intent>,
        packageName: String
    ) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${packageName}")
        )
        launcher.launch(intent)
    }

    fun checkOverlayPermission(isOverlayPermissionGranted: Boolean) = viewModelScope.launch {
        if (isOverlayPermissionGranted) _permissionState.value =
            PermissionState.OverlayPermissionGranted()
        else _permissionState.value = PermissionState.OverlayPermissionDenied()
    }

    fun startViewService(activity: Activity) {
        ViewService.launch(activity)
    }
}