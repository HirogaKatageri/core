package com.hirogakatageri.sandbox.profile

import androidx.core.view.isVisible
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.hirogakatageri.core.activity.CoreViewModelActivity
import com.hirogakatageri.sandbox.navigation.NavigationUtil.UID
import com.hirogakatageri.sandbox.navigation.NavigationUtil.USERNAME
import com.hirogakatageri.core.utils.listener.ThrottledOnClickListener
import com.hirogakatageri.sandbox.profile.databinding.ProfileActivityMainBinding
import com.hirogakatageri.sandbox.utils.SpannableUtil.bold
import com.hirogakatageri.sandbox.utils.SpannableUtil.normal
import com.hirogakatageri.sandbox.utils.SpannableUtil.plus
import com.hirogakatageri.sandbox.utils.SpannableUtil.spannable
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivityMain :
    CoreViewModelActivity<ProfileActivityMainBinding, ProfileActivityMainViewModel>() {

    private val username: String get() = intent.extras?.getString(USERNAME) ?: ""
    private val uid: Int get() = intent.extras?.getInt(UID) ?: -1

    override val viewModel: ProfileActivityMainViewModel by viewModel()

    override fun createBinding(): ProfileActivityMainBinding =
        ProfileActivityMainBinding.inflate(layoutInflater)

    override suspend fun ProfileActivityMainBinding.bind() {
        toolbar.setNavigationIcon(R.drawable.ic_round_arrow_back_24)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        attachClickListener()

        viewModel.username = username
        viewModel.uid = uid

        viewModel.isLoading.observe {
            progressContent.isVisible = it
        }

        viewModel.isNoteUpdatedFinished.observe {
            if (it) Snackbar.make(
                root,
                R.string.profile_save_note_finish,
                Snackbar.LENGTH_SHORT
            ).show()
        }

        viewModel.network.observe { isAvailable ->
            txtNetworkStatus.isVisible = !isAvailable
            if (isAvailable) viewModel.retryQuery()
        }

        viewModel.userModel.observe { model ->
            if (model == null) Snackbar.make(
                root,
                R.string.profile_query_error,
                Snackbar.LENGTH_SHORT
            ).show()
            else {
                imgCover.load(model.profileImageUrl)
                txtFollowers.text = getString(R.string.profile_followers, model.followers)
                txtFollowings.text = getString(R.string.profile_following, model.followings)

                txtName.text = spannable {
                    normal(bold(getString(R.string.profile_name)) + " ${model.name ?: ""}")
                }

                txtCompany.isVisible = !model.companyName.isNullOrBlank()
                txtCompany.text = spannable {
                    normal(bold(getString(R.string.profile_company)) + " ${model.companyName ?: ""}")
                }

                txtBlog.isVisible = !model.blogUrl.isNullOrBlank()
                txtBlog.text = spannable {
                    normal(bold(getString(R.string.profile_blog)) + " ${model.blogUrl ?: ""}")
                }

                txtNoteInput.setText(model.notes)
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun attachClickListener() = binding {
        ThrottledOnClickListener.Builder(
            lifecycleOwner = this@ProfileActivityMain,
            view = btnSaveNotes,
            onClick = {
                when (it?.id) {
                    R.id.btn_save_notes -> {
                        viewModel.updateNotes(txtNoteInput.text.toString())
                        setResult(RESULT_OK)
                    }
                }
            }
        ).build()
    }

}