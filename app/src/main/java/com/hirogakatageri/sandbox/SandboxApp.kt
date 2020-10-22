package com.hirogakatageri.sandbox

import com.hirogakatageri.core.BaseApplication
import com.hirogakatageri.home.HomeModule
import com.hirogakatageri.repository.RepositoryModule
import org.koin.core.module.Module
import org.koin.dsl.module

class SandboxApp : BaseApplication() {

    override val moduleList: List<Module> = listOf(
        coreModule(),
        RepositoryModule.create(),
        HomeModule.create()
    )
}