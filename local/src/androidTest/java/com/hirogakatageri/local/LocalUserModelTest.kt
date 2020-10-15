package com.hirogakatageri.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hirogakatageri.local.dao.UserDao
import com.hirogakatageri.local.model.LocalUserModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class LocalUserModelTest {

    private val sampleModels = listOf<LocalUserModel>(
        LocalUserModel(1, "Hiroga"),
        LocalUserModel(2, "Rena"),
        LocalUserModel(3, "Kira"),
        LocalUserModel(4, "Rika")
    )
    private lateinit var database: LocalDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java).build()
        userDao = database.userDao()
    }

    @After
    fun onFinish() {
        database.close()
    }

    @Test
    fun test_Insert() {
        userDao.addUsers(sampleModels)

        val users = userDao.getUsers(0)

        Assert.assertEquals("Hiroga", users[0].username)
        Assert.assertEquals(4, users.size)
    }

    @Test
    fun test_Search() {
        userDao.addUsers(sampleModels)

        var toSearch = "Hiroga"
        var users = userDao.search(toSearch)
        Assert.assertEquals(1, users.size)

        toSearch = "R*"
        users = userDao.search(toSearch)
        Assert.assertEquals(2, users.size)
    }

    @Test
    fun test_Update() {
        userDao.addUsers(sampleModels)
        val users = userDao.getAllUsers()

        val newUsername = "Hamster"

        users[0].copy(username = newUsername).let { userDao.updateUser(it) }
        userDao.updateUser(LocalUserModel(0, newUsername))

        val newList = userDao.getAllUsers()
        Assert.assertEquals(newUsername, newList[0].username)
    }

}