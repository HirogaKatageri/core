package com.hirogakatageri.home.env

import com.hirogakatageri.core.BaseApplication
import com.hirogakatageri.home.HomeModule
import com.hirogakatageri.home.R
import com.hirogakatageri.repository.RepositoryModule
import org.koin.core.module.Module

class App : BaseApplication() {

    override val moduleList: List<Module> = listOf(
        RepositoryModule.create(),
        HomeModule.create()
    )
}