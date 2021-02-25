package com.hirogakatageri.core.sample

import com.hirogakatageri.core.CoreApp
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

class SampleApp : CoreApp() {

    private val sampleMod = module {

        scope<SampleActivity> {
            fragment { SampleFragment() }
        }

        scope<SampleFragment> { }

        viewModel { SampleViewModel() }
    }

    override val moduleList: List<Module> = listOf(sampleMod)

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}