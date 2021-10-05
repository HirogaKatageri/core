package dev.hirogakatageri.sandbox.ui.feature

import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.material.snackbar.Snackbar
import dev.hirogakatageri.core.fragment.CoreViewModelFragment
import dev.hirogakatageri.sandbox.databinding.FragmentMainBinding
import dev.hirogakatageri.sandbox.ui.PermissionState
import dev.hirogakatageri.sandbox.ui.main.ParentViewModel
import dev.hirogakatageri.sandbox.util.canDrawOverlays
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class FeatureFragment : CoreViewModelFragment<FragmentMainBinding, FeatureViewModel>() {

    var fragmentState: FeatureFragmentState? = null

    private val servicePermissionLauncher =
        registerForActivityResult(RequestMultiplePermissions()) { map ->
            vm.verifyServiceViewPermissions(map)
        }

    private val overlayPermissionLauncher =
        registerForActivityResult(StartActivityForResult()) {
            vm.checkOverlayPermission(Settings.canDrawOverlays(requireActivity()))
        }

    private val authLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            vm.verifyFirebaseAuth(result)
        }

    private val pvm: ParentViewModel by sharedViewModel()

    override val vm: FeatureViewModel by stateViewModel()

    private val redirectionCallback: RedirectionCallback by lazy { RedirectionCallback() }

    private val sampleAdapter: FeatureAdapter by scope.inject()

    private var layoutManager: LinearLayoutManager? = null

    override fun createBinding(container: ViewGroup?): FragmentMainBinding =
        FragmentMainBinding.inflate(layoutInflater, container, false)

    override fun FragmentMainBinding.bind() {
        setupRecyclerView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            launch { observeState() }
            launch { observePermissionState() }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        // Retrieve and Restore Saved Instance States of Views
        val featureListState = vm.featureListState

        if (featureListState != null) {
            layoutManager?.onRestoreInstanceState(featureListState)
        }
    }

    override fun onDestroyView() {
        // Save Instance States of Views
        vm.featureListState = layoutManager?.onSaveInstanceState()
        layoutManager = null
        binding?.listFeatures?.adapter = null
        super.onDestroyView()
    }

    private fun FragmentMainBinding.setupRecyclerView() {
        sampleAdapter.clickCallback = redirectionCallback
        layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        listFeatures.layoutManager = layoutManager
        listFeatures.adapter = sampleAdapter
    }

    private suspend fun observeState() = withContext(Dispatchers.Main) {
        vm.state.collect { state ->
            if (state != fragmentState) {
                when (state) {
                    is FeatureFragmentState.Default -> onDefaultState(state)
                    is FeatureFragmentState.UserSignedIn -> onUserSignedIn(state)
                    is FeatureFragmentState.UserSignedOut -> onUserSignedOut(state)
                }

                fragmentState = state
            }
        }
    }

    private fun onDefaultState(
        state: FeatureFragmentState.Default
    ) = binding {
        val message: String =
            state.msgResId?.let { resId -> getString(resId) } ?: state.message ?: ""

        if (message.isNotBlank())
            Snackbar.make(
                root,
                message,
                Snackbar.LENGTH_SHORT
            ).show()
    }

    private fun onUserSignedIn(
        state: FeatureFragmentState.UserSignedIn
    ) = binding {
        val message: String = getString(state.msgResId, state.email)

        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun onUserSignedOut(
        state: FeatureFragmentState.UserSignedOut
    ) = binding {
        Snackbar.make(root, state.msgResId, Snackbar.LENGTH_SHORT).show()
    }

    private suspend fun observePermissionState() = withContext(Dispatchers.Main) {
        vm.permissionState.collect { state ->
            when (state) {
                is PermissionState.Neutral -> Unit
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

    inner class RedirectionCallback() : FeatureAdapter.FeatureItemClickCallback() {

        override fun onClick(model: FeatureModel) {
            when (model.key) {
                FeatureManager.FeatureKey.CLOCK -> pvm.showTimeFragment()
                FeatureManager.FeatureKey.OAUTH -> pvm.showOAuthFragment()
                FeatureManager.FeatureKey.VIEW_SERVICE ->
                    vm.startServiceView(servicePermissionLauncher)
                FeatureManager.FeatureKey.FCM -> pvm.showFcmFragment()
                FeatureManager.FeatureKey.FIREBASE_AUTH ->
                    vm.signInWithFirebase(authLauncher)
                FeatureManager.FeatureKey.API_VERIFY_USER -> vm.verifyUser()
                FeatureManager.FeatureKey.CHAT -> pvm.showChatFragment()
            }
        }
    }
}
