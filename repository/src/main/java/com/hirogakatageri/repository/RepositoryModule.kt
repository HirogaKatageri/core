package com.hirogakatageri.repository

import androidx.room.Room
import com.hirogakatageri.local.LocalDatabase
import com.hirogakatageri.local.dao.UserDao
import com.hirogakatageri.remote.Client
import com.hirogakatageri.remote.service.MainService
import org.koin.android.ext.koin.androidApplication
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
        single<Client> { Client(androidContext()) }
        single<MainService> { (client: Client) -> client.createService() }
        factory<UserDao> { (db: LocalDatabase) -> db.userDao() }
    }

}