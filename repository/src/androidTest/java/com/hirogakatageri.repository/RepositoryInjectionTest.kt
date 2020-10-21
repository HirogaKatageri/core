package com.hirogakatageri.repository

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hirogakatageri.local.LocalDatabase
import com.hirogakatageri.local.dao.UserDao
import com.hirogakatageri.remote.Client
import com.hirogakatageri.remote.service.MainService
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.get
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule

@RunWith(AndroidJUnit4::class)
class RepositoryInjectionTest : KoinTest {

    @get:Rule
    val koinRule = KoinTestRule.create {
        androidContext(ApplicationProvider.getApplicationContext())
        modules(RepositoryModule.create())
    }

    @After
    fun onFinish() {
        stopKoin()
    }

    @Test
    fun test_Injection() {
        val localDatabase: LocalDatabase = get()
        val client: Client = get()

        Assert.assertNotNull(localDatabase)
        Assert.assertNotNull(client)

        val userDao: UserDao = get()
        val service: MainService = get()

        Assert.assertNotNull(userDao)
        Assert.assertNotNull(service)

        val userListRepository: UserRepository = get()
        val userRepository: UserRepository = get()

        Assert.assertNotNull(userListRepository)
        Assert.assertNotNull(userRepository)
    }
}