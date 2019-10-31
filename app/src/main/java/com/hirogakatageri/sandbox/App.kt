package com.hirogakatageri.sandbox

import com.hirogakatageri.auth.AuthRepository
import com.hirogakatageri.auth.AuthViewModel
import com.hirogakatageri.base.BaseApp
import com.hirogakatageri.blog.list.BlogPostListRepository
import com.hirogakatageri.blog.list.BlogPostListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class App : BaseApp() {

    override fun setupModules() {
        super.setupModules()

        val authModule = module {
            single { AuthRepository() }
            viewModel { AuthViewModel(get()) }
        }

        val blogModule = module {
            single { BlogPostListRepository(get(), get()) }
            viewModel { BlogPostListViewModel(get(), get()) }
        }

        modules.run {
            add(authModule)
            add(blogModule)
        }
    }
}