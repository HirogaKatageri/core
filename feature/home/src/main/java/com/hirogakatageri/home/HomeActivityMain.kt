package com.hirogakatageri.home

import com.hirogakatageri.core.activity.BaseViewModelActivity
import com.hirogakatageri.home.databinding.HomeActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivityMain : BaseViewModelActivity<HomeActivityMainBinding, HomeActivityMainViewModel>() {

    override val viewModel: HomeActivityMainViewModel by viewModel()
    override val binding: HomeActivityMainBinding by lazy { inflate<HomeActivityMainBinding>() }

    override suspend fun HomeActivityMainBinding.bind() {
        TODO("Not yet implemented")
    }
}