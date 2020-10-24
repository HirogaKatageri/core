package com.hirogakatageri.sandbox.remote

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hirogakatageri.sandbox.remote.call.RemoteHelpers
import com.hirogakatageri.sandbox.remote.service.MainService
import com.hirogakatageri.sandbox.remote.wrapper.parse
import com.hirogakatageri.sandbox.remote.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE, sdk = [21])
class MainServiceTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var mainService: MainService
    private var isLoading = false

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val context = ApplicationProvider.getApplicationContext<Application>()
        mainService = Client(context, isTesting = true).createService()
        isLoading = false
    }

    @After
    fun onFinish() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun test_GetUserList() = runBlocking {
        isLoading = true

        mainService.getUsers(0).parse(
            onError = { error ->
                assertNull(error)
            }, onSuccess = { headers, response ->
                isLoading = false

                val offset = RemoteHelpers.getNextOffsetOfUsers(headers)

                assertNotNull("headers", headers)
                assertNotNull("link", headers.get("link"))
                assertNotNull("response", response)
                assertNotNull("offset", offset)
            }
        )

        delay(2500)
        assertFalse("Timed Out", isLoading)
    }

    @Test
    fun test_getUserInfo() = runBlocking {
        isLoading = true

        mainService.getUsers(0).parse(
            onError = { error -> assertNull("An error occurred", error) },
            onSuccess = { headers, list ->

                val username = list[0].login

                mainService.getUser(username).parse(
                    onError = { error -> assertNull("An error occurred", error) },
                    onSuccess = { headers, remoteUserModel ->

                        assertEquals("username", username, remoteUserModel.login)
                        assertNotNull("model", remoteUserModel)
                        isLoading = false
                    }
                )

            }
        )

        delay(2500)
        assertFalse("Timed Out", isLoading)
    }

}