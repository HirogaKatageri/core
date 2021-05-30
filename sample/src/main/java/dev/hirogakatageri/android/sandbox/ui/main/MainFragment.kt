package dev.hirogakatageri.android.sandbox.ui.main

import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import dev.hirogakatageri.android.sandbox.databinding.FragmentMainBinding
import dev.hirogakatageri.android.sandbox.ui.PermissionState
import dev.hirogakatageri.android.sandbox.util.canDrawOverlays
import dev.hirogakatageri.core.fragment.CoreViewModelFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment : CoreViewModelFragment<FragmentMainBinding, MainViewModel>() {

    private val viewServicePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            vm.verifyPermissionServiceViewPermissions(map)
        }

    private val overlayPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            vm.checkOverlayPermission(Settings.canDrawOverlays(requireActivity()))
        }

    override val vm: MainViewModel by sharedViewModel()

    override fun createBinding(container: ViewGroup?): FragmentMainBinding =
        FragmentMainBinding.inflate(layoutInflater, container, false)

    override fun FragmentMainBinding.bind() {
        btnViewService.setOnClickListener {
            vm.requestServiceViewPermissions(
                viewServicePermissionLauncher
            )
        }
        btnOauth.setOnClickListener { vm.showOAuthFragment() }
        btnShowTime.setOnClickListener { vm.showTimeFragment() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            launch { observePermissionState() }
        }
    }

    private suspend fun observePermissionState() = withContext(Dispatchers.Main) {
        vm.permissionState.collect { state ->
            when (state) {
                is PermissionState.ServiceViewPermissionsGranted -> if (!canDrawOverlays) vm.requestOverlayPermission(
                    overlayPermissionLauncher,
                    requireActivity().packageName
                ) else vm.startViewService(requireActivity())
                is PermissionState.ServiceViewPermissionsDenied -> onServiceViewPermissionsRejected()
                is PermissionState.OverlayPermissionGranted -> vm.startViewService(requireActivity())
                is PermissionState.OverlayPermissionDenied -> onOverlayPermissionRejected()
                else -> Unit
            }
            vm.resetPermissionState()
        }
    }

    private fun onServiceViewPermissionsRejected() = binding {
        Snackbar
            .make(root, "Permissions not granted", Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun onOverlayPermissionRejected() = binding {
        Snackbar
            .make(root, "Overlay permission is required", Snackbar.LENGTH_SHORT)
            .setAction("Open Settings") {
                vm.requestOverlayPermission(
                    overlayPermissionLauncher,
                    requireActivity().packageName
                )
            }
            .show()
    }
}