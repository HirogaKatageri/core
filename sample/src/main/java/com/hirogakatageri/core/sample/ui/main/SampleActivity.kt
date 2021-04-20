package com.hirogakatageri.core.sample.ui.main

import android.os.Bundle
import com.hirogakatageri.core.activity.CoreViewModelActivity
import com.hirogakatageri.core.sample.databinding.ActivitySampleBinding
import org.koin.androidx.fragment.android.replace
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.viewmodel.ext.android.viewModel

class SampleActivity : CoreViewModelActivity<ActivitySampleBinding, SampleViewModel>() {

    override val vm: SampleViewModel by viewModel()

    override fun createBinding(): ActivitySampleBinding =
        ActivitySampleBinding.inflate(layoutInflater)

    override fun ActivitySampleBinding.bind() {
        supportFragmentManager.beginTransaction()
            .replace<SampleFragment>(fragmentContainer.id)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupKoinFragmentFactory()
        super.onCreate(savedInstanceState)
    }
}