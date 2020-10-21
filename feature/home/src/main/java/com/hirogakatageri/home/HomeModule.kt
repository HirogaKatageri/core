package com.hirogakatageri.home

import com.hirogakatageri.utils.GenericEpoxyViewBindingClickListener
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object HomeModule {

    fun create() = module {
        scope<HomeActivityMain> {
            scoped { (listener: GenericEpoxyViewBindingClickListener) ->
                HomeActivityMainController(listener)
            }
        }
        viewModel { HomeActivityMainViewModel(get()) }
    }

}