package dev.hirogakatageri.android.sandbox

import dev.hirogakatageri.android.sandbox.ui.main.MainActivity
import dev.hirogakatageri.android.sandbox.ui.main.MainFragment
import dev.hirogakatageri.android.sandbox.ui.main.MainViewModel
import dev.hirogakatageri.android.sandbox.ui.time.TimeFragment
import dev.hirogakatageri.android.sandbox.util.Clock
import com.jakewharton.threetenabp.AndroidThreeTen
import dev.hirogakatageri.core.CoreApp
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