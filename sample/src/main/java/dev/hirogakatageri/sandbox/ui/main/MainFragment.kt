package dev.hirogakatageri.sandbox.ui.main

import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.github.ajalt.timberkt.Timber.d
import com.google.android.material.snackbar.Snackbar
import dev.hirogakatageri.core.fragment.CoreViewModelFragment
import dev.hirogakatageri.sandbox.R
import dev.hirogakatageri.sandbox.databinding.FragmentMainBinding
import dev.hirogakatageri.sandbox.ui.PermissionState
import dev.hirogakatageri.sandbox.util.canDrawOverlays
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainFragment : CoreViewModelFragment<FragmentMainBinding, MainViewModel>() {

    private val viewServicePermissionLauncher =
        registerForActivityResult(RequestMultiplePermissions()) { map ->
            vm.verifyPermissionServiceViewPermissions(map)
        }

    private val overlayPermissionLauncher =
        registerForActivityResult(StartActivityForResult()) {
            vm.checkOverlayPermission(Settings.canDrawOverlays(requireActivity()))
        }

    private val pvm: ParentViewModel by sharedViewModel()

    override val vm: MainViewModel by viewModel {
        parametersOf(viewServicePermissionLauncher)
    }

    private val redirectionClickListener: RedirectionClickListener by inject {
        parametersOf(pvm, vm)
    }

    override fun createBinding(container: ViewGroup?): FragmentMainBinding =
        FragmentMainBinding.inflate(layoutInflater, container, false)

    override fun FragmentMainBinding.bind() {
        btnShowTime.setOnClickListener(redirectionClickListener)
        btnOauth.setOnClickListener(redirectionClickListener)
        btnViewService.setOnClickListener(redirectionClickListener)
        btnFcm.setOnClickListener(redirectionClickListener)
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
                is PermissionState.ServiceViewPermissionsGranted ->
                    if (!canDrawOverlays)
                        vm.requestOverlayPermission(
                            overlayPermissionLauncher,
                            requireActivity().packageName
                        )
                    else
                        vm.startViewService(requireActivity())
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

    internal class RedirectionClickListener(
        private val pvm: ParentViewModel,
        private val vm: MainViewModel
    ) : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btn_show_time -> pvm.showTimeFragment()
                R.id.btn_oauth -> pvm.showOAuthFragment()
                R.id.btn_view_service -> vm.startServiceView()
                R.id.btn_fcm -> pvm.showFcmFragment()
            }
        }
    }
}
