package com.hirogakatageri.auth

import android.app.Activity
import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.hirogakatageri.base.BaseActivity
import com.hirogakatageri.base.util.IntentUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val RC_SIGN_IN = 100

class AuthActivity : BaseActivity<AuthViewModel>() {

    override val titleResId: Int?
        get() = R.string.authentication
    override val layoutResId: Int?
        get() = R.layout.activity_auth
    override val model: AuthViewModel by viewModel()

    override fun loadContent() {
        showAuth()
    }

    private fun showAuth() {
        val signInProviders = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(signInProviders)
                .build(), RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SIGN_IN -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val intent = IntentUtil.createBlogListIntent(this)
                        startActivity(intent)
                        finish()
                    }
                    Activity.RESULT_CANCELED -> {
                        showAuth()
                    }
                }
            }
        }
    }
}