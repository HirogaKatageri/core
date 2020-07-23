package com.hirogakatageri.sandbox

import android.view.ViewGroup
import com.hirogakatageri.base.fragment.BaseFragment
import com.hirogakatageri.sandbox.databinding.ActivityMainBinding

class MainFragment : BaseFragment<ActivityMainBinding>() {

    override fun createBinding(container: ViewGroup?): ActivityMainBinding =
        inflate<ActivityMainBinding>(container)

    override suspend fun ActivityMainBinding.bind() {

    }

    override suspend fun afterBind() {

    }
}