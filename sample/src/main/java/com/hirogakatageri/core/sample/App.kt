package com.hirogakatageri.core.sample

import com.hirogakatageri.core.CoreApp
import com.hirogakatageri.core.sample.ui.main.MainActivity
import com.hirogakatageri.core.sample.ui.main.MainFragment
import com.hirogakatageri.core.sample.ui.main.MainViewModel
import com.hirogakatageri.core.sample.ui.time.TimeFragment
import com.hirogakatageri.core.sample.util.Clock
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val mainModule = module {

    factory { Clock() }

    viewModel { MainViewModel(get()) }

    scope<MainActivity> {
        fragment { MainFragment() }
        fragment { TimeFragment() }
    }

    scope<MainFragment> { }
    scope<TimeFragment> { }
}

class App : CoreApp() {

    override val moduleList: List<Module> = listOf(mainModule)

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}