package com.hirogakatageri.sandbox.repository

import androidx.room.Room
import com.hirogakatageri.sandbox.local.LocalDatabase
import com.hirogakatageri.sandbox.local.dao.UserDao
import com.hirogakatageri.sandbox.remote.Client
import com.hirogakatageri.sandbox.remote.service.GithubService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object RepositoryModule {

    fun create() = module {
        single<LocalDatabase> {
            Room.databaseBuilder(
                androidContext(),
                LocalDatabase::class.java,
                "local-db"
            ).build()
        }
        factory<UserDao> { get<LocalDatabase>().userDao() }

        single<Client> { Client(androidContext()) }
        single<GithubService> { get<Client>().createGithubService() }

        single { UserListRepository(get(), get()) }
        single { UserRepository(get(), get()) }
    }

}