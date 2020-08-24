package com.hirogakatageri.sandbox

import com.hirogakatageri.core.activity.BaseViewModelActivity
import com.hirogakatageri.sandbox.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseViewModelActivity<ActivityMainBinding, MainViewModel>() {

    override val viewModel: MainViewModel by viewModel()

    override val binding: ActivityMainBinding by lazy { inflate<ActivityMainBinding>() }

    override suspend fun ActivityMainBinding.bind() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container,
                MainFragment()
            )
            .commit()
    }
}