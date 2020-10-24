package com.hirogakatageri.sandbox.profile

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ProfileModule {

    fun create() = module {
        scope<ProfileActivityMain> {
            scoped { }
        }

        viewModel { ProfileActivityMainViewModel(get(), get()) }
    }

}