package com.hirogakatageri.sandbox.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hirogakatageri.sandbox.local.dao.UserDao
import com.hirogakatageri.sandbox.local.model.LocalUserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class LocalUserModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var sample: MutableList<LocalUserModel>
    private lateinit var database: LocalDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java).build()
        userDao = database.userDao()
        sample = mutableListOf()

        for (i in 0..5) {
            sample.add(LocalUserModel(i, "${UUID.randomUUID()}"))
        }
    }

    @After
    fun onFinish() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        database.close()
    }

    @Test
    fun test_InsertAndGet() = runBlocking {
        userDao.insertUsers(*sample.toTypedArray())
        delay(200)

        val models = userDao.getAllUsers()
        delay(200)
        assertEquals(sample.size, models.size)

        val limitedList = userDao.getUsers(0, 1)
        delay(200)
        assertEquals(1, limitedList.size)

        val nullModel = userDao.getUser("")
        delay(200)
        assertNull(nullModel)
    }

    @Test
    fun test_Search() = runBlocking {
        userDao.insertUsers(
            LocalUserModel(0, "Hiroga"),
            LocalUserModel(1, "Rena"),
            LocalUserModel(2, "Kira"),
            LocalUserModel(3, "Rika")
        )
        delay(200)

        userDao.search("Hiroga").let {
            delay(200)
            assertEquals(1, it.size)
        }

        userDao.search("R").let {
            delay(200)
            assertEquals(2, it.size)
        }
    }

    @Test
    fun test_Update() = runBlocking {
        val newModel = LocalUserModel(sample[0].uid, username = "Hamster")

        userDao.insertUsers(*sample.toTypedArray())
        delay(200)

        userDao.updateUsers(newModel)
        delay(200)

        val updatedModel = userDao.getUser("Hamster")
        delay(200)

        assertEquals(updatedModel, newModel)
        assertEquals("Hamster", updatedModel?.username)
    }

}