package com.hirogakatageri.base

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hirogakatageri.data.repository.BlogRepository
import com.hirogakatageri.data.repository.ImageRepository
import com.hirogakatageri.data.repository.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

open class BaseApp : Application() {

    val modules: MutableList<Module> = mutableListOf()

    open fun setupModules() {

        val firebaseModule = module {
            single { FirebaseAuth.getInstance() }
            single { FirebaseFirestore.getInstance() }
            single { FirebaseStorage.getInstance() }
        }

        val repoModule = module {
            single { BlogRepository(get()) }
            single { UserRepository(get(), get()) }
            single { ImageRepository(get()) }
        }

        modules.run {
            add(firebaseModule)
            add(repoModule)
        }
    }

    override fun onCreate() {
        super.onCreate()
        setupModules()
        startKoin {
            androidContext(this@BaseApp)
            modules(modules)
        }
    }
}