package com.hirogakatageri.repository

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hirogakatageri.local.LocalDatabase
import com.hirogakatageri.local.dao.UserDao
import com.hirogakatageri.remote.Client
import com.hirogakatageri.remote.service.MainService
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class InjectionTest : KoinTest {

    @Test
    fun test_RepositoryInjection() {
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(RepositoryModule.create())
        }

        val localDatabase: LocalDatabase = get()
        val userDao: UserDao = get { parametersOf(localDatabase) }
        val client: Client = get()
        val service: MainService = get { parametersOf(client) }

        Assert.assertNotNull(localDatabase)
        Assert.assertNotNull(userDao)
        Assert.assertNotNull(client)
        Assert.assertNotNull(service)
    }

}