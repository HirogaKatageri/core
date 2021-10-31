package dev.hirogakatageri.core.components

import dev.hirogakatageri.core.CoreApp
import org.koin.core.module.Module
import org.koin.dsl.module

val testMod = module {
    scope<ViewBindingTestActivity> {}
    scope<ViewBindingTestFragment> {}
}

class TestApp : CoreApp() {
    override val moduleList: List<Module> = listOf(testMod)
}
