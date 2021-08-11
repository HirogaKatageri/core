package dev.hirogakatageri.core.components

import dev.hirogakatageri.core.CoreApp
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val testMod = module {
    scope<TestActivity> {}
    scope<TestViewModelActivity> {}
    scope<TestFragment> {}
    scope<TestViewModelFragment> {}
    viewModel { TestViewModel() }
}

class TestApp : CoreApp() {
    override val moduleList: List<Module> = listOf(testMod)
}
