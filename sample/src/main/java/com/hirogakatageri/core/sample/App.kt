package com.hirogakatageri.core.sample

import com.hirogakatageri.core.CoreApp
import com.hirogakatageri.core.sample.ui.main.SampleActivity
import com.hirogakatageri.core.sample.ui.main.SampleFragment
import com.hirogakatageri.core.sample.ui.main.SampleViewModel
import com.hirogakatageri.core.sample.util.Clock
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val mainModule = module {

    factory { Clock() }

    viewModel { SampleViewModel(get()) }

    scope<SampleActivity> {
        fragment { SampleFragment() }
    }

    scope<SampleFragment> { }
}

class App : CoreApp() {

    override val moduleList: List<Module> = listOf(mainModule)

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}