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
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.ajalt.timberkt.Timber.d
import com.haroldadmin.cnradapter.NetworkResponse
import dev.hirogakatageri.sandbox.PermissionLauncher
import dev.hirogakatageri.sandbox.R
import dev.hirogakatageri.sandbox.data.FirebaseManager
import dev.hirogakatageri.sandbox.data.repository.ApiRepository
import dev.hirogakatageri.sandbox.service.SampleViewService
import dev.hirogakatageri.sandbox.ui.PermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FeatureViewModel(
    private val savedState: SavedStateHandle,
    private val apiRepository: ApiRepository,
    private val firebase: FirebaseManager
) : ViewModel() {

    private val _state: MutableStateFlow<FeatureFragmentState> =
        MutableStateFlow(FeatureFragmentState.Default())
    val state: StateFlow<FeatureFragmentState> = _state

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

    fun resetPermissionState() = viewModelScope.launch {
        _permissionState.value = PermissionState.Neutral()
    }

    fun startServiceView(launcher: PermissionLauncher) = viewModelScope.launch {
        requestServiceViewPermissions(launcher)
    }

    private suspend fun requestServiceViewPermissions(launcher: PermissionLauncher) =
        withContext(Dispatchers.Main) {
            val permissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
            launcher.launch(permissions)
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

    fun signInWithFirebase(launcher: ActivityResultLauncher<Intent>) {
        val providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        launcher.launch(intent)
    }

    fun verifyFirebaseAuth(
        result: FirebaseAuthUIAuthenticationResult
    ) = viewModelScope.launch {
        d { "Retrieving Current User..." }
        val user = firebase.currentUser

        val nextState: FeatureFragmentState =
            if (user != null) FeatureFragmentState.UserSignedIn(
                user.email ?: "******"
            )
            else FeatureFragmentState.UserSignedOut(
                msgResId = R.string.msg_user_sign_in_failed
            )

        _state.value = nextState
    }

    fun verifyUser() = viewModelScope.launch {
        val user = firebase.currentUser
        val idTokenResult = user?.getIdToken(true)?.await()

        when (val response = apiRepository.verifyUser(idTokenResult?.token)) {
            is NetworkResponse.Success -> _state.value = FeatureFragmentState.Default(
                msgResId = R.string.msg_api_verify_user_success
            )
            is NetworkResponse.Error -> _state.value = FeatureFragmentState.Default(
                msgResId = R.string.msg_api_verify_user_failed
            )
        }
    }

    companion object {
        const val FEATURE_LIST_STATE = "FEATURE_LIST_STATE"
    }
}
