package dev.hirogakatageri.sandbox.ui.main

import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.hirogakatageri.core.fragment.CoreViewModelFragment
import dev.hirogakatageri.sandbox.databinding.FragmentMainBinding
import dev.hirogakatageri.sandbox.ui.PermissionState
import dev.hirogakatageri.sandbox.ui.main.feature.FeatureAdapter
import dev.hirogakatageri.sandbox.ui.main.feature.FeatureManager
import dev.hirogakatageri.sandbox.ui.main.feature.FeatureModel
import dev.hirogakatageri.sandbox.util.canDrawOverlays
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class MainFragment : CoreViewModelFragment<FragmentMainBinding, MainViewModel>() {

    private val servicePermissionLauncher =
        registerForActivityResult(RequestMultiplePermissions()) { map ->
            vm.verifyServiceViewPermissions(map)
        }

    private val overlayPermissionLauncher =
        registerForActivityResult(StartActivityForResult()) {
            vm.checkOverlayPermission(Settings.canDrawOverlays(requireActivity()))
        }

    private val pvm: ParentViewModel by sharedViewModel()

    override val vm: MainViewModel by stateViewModel {
        parametersOf(servicePermissionLauncher)
    }

    private val redirectionCallback: RedirectionCallback by scope.inject {
        parametersOf(pvm, vm)
    }

    private val sampleAdapter: FeatureAdapter by scope.inject()

    private lateinit var layoutManager: LinearLayoutManager

    override fun createBinding(container: ViewGroup?): FragmentMainBinding =
        FragmentMainBinding.inflate(layoutInflater, container, false)

    override fun FragmentMainBinding.bind() {
        setupRecyclerView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            launch { observePermissionState() }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val featureListState = vm.featureListState

        if (featureListState != null) {
            layoutManager.onRestoreInstanceState(featureListState)
        }
    }

    override fun onDestroyView() {
        vm.featureListState = layoutManager.onSaveInstanceState()
        binding?.listFeatures?.adapter = null
        super.onDestroyView()
    }

    private fun FragmentMainBinding.setupRecyclerView() {
        sampleAdapter.clickCallback = redirectionCallback
        layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        listFeatures.layoutManager = layoutManager
        listFeatures.adapter = sampleAdapter
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
                is PermissionState.OverlayPermissionGranted -> onOverlayPermissionGranted()
                is PermissionState.OverlayPermissionDenied -> onOverlayPermissionDenied()
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

    private fun onOverlayPermissionGranted() = lifecycleScope.launchWhenStarted {
        vm.startViewService(requireActivity())
    }

    private fun onOverlayPermissionDenied() = binding {
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

    internal class RedirectionCallback(
        private val pvm: ParentViewModel,
        private val vm: MainViewModel
    ) : FeatureAdapter.SampleItemClickCallback() {

        override fun onClick(model: FeatureModel) {
            when (model.key) {
                FeatureManager.FeatureKey.CLOCK -> pvm.showTimeFragment()
                FeatureManager.FeatureKey.OAUTH -> pvm.showOAuthFragment()
                FeatureManager.FeatureKey.VIEW_SERVICE -> vm.startServiceView()
                FeatureManager.FeatureKey.FCM -> pvm.showFcmFragment()
            }
        }
    }
}
