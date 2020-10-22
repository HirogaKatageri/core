package com.hirogakatageri.profile

import com.hirogakatageri.core.activity.BaseViewModelActivity
import com.hirogakatageri.profile.databinding.ProfileActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivityMain:
    BaseViewModelActivity<ProfileActivityMainBinding, ProfileActivityMainViewModel>() {

    override val viewModel: ProfileActivityMainViewModel by viewModel()

    override fun createBinding(): ProfileActivityMainBinding =
        ProfileActivityMainBinding.inflate(layoutInflater)

    override suspend fun ProfileActivityMainBinding.bind() {

    }
}