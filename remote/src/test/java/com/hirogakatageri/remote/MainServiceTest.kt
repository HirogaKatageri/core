package com.hirogakatageri.remote

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hirogakatageri.remote.call.RemoteHelpers
import com.hirogakatageri.remote.service.MainService
import com.hirogakatageri.remote.wrapper.parse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE, sdk = [21])
class MainServiceTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = TestCoroutineScope(testDispatcher)
    private lateinit var mainService: MainService
    private var isLoading = false

    @Before
    fun setup() {
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
    fun `Get User List`() {
        coroutineScope.launch {
            isLoading = true

            mainService.getUsers(0).parse(
                onError = { error ->
                    Assert.assertNull("An error occurred", error)
                }, onSuccess = { headers, response ->

                    println("Success")

                    isLoading = false

                    val link = headers.get("link")
                    println("Link: $link")

                    val nextPage = RemoteHelpers.getNextPageOfUsers(headers)

                    println("Next Page: $nextPage")

                    Assert.assertNotNull("Headers isNull", headers)
                    Assert.assertNotNull("Link isNull", link)
                    Assert.assertNotNull("Next Page isNull", nextPage)
                    Assert.assertNotNull("Response isNull", response)
                }
            )
        }
        Thread.sleep(2500)
        Assert.assertFalse("isStillLoading", isLoading)
    }

}