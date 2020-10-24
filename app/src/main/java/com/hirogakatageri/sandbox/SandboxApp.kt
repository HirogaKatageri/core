package com.hirogakatageri.sandbox

import coil.annotation.ExperimentalCoilApi
import com.hirogakatageri.core.CoreApplication
import com.hirogakatageri.sandbox.home.HomeModule
import com.hirogakatageri.sandbox.profile.ProfileModule
import com.hirogakatageri.sandbox.repository.RepositoryModule
import org.koin.core.KoinExperimentalAPI
import org.koin.core.module.Module

@KoinExperimentalAPI
@ExperimentalCoilApi
class SandboxApp : CoreApplication() {

    override val moduleList: List<Module> = listOf(
        coreModule(),
        RepositoryModule.create(),
        HomeModule.create(),
        ProfileModule.create()
    )
}