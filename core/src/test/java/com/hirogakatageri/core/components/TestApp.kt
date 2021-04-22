package com.hirogakatageri.core.components

import com.hirogakatageri.core.CoreApp
import org.koin.core.module.Module
import org.koin.dsl.module

val testMod = module {

    scope<TestActivity> { }

}

class TestApp : CoreApp() {
    override val moduleList: List<Module> = listOf(testMod)
}