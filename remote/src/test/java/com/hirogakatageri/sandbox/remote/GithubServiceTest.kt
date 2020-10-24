package com.hirogakatageri.sandbox.remote

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hirogakatageri.sandbox.remote.call.RemoteHelpers
import com.hirogakatageri.sandbox.remote.service.GithubService
import com.hirogakatageri.sandbox.remote.wrapper.parse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE, sdk = [21])
class GithubServiceTest {

    private lateinit var mockServer: MockWebServer
    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var githubService: GithubService

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val context = ApplicationProvider.getApplicationContext<Application>()
        mockServer = MockWebServer()
        mockServer.start()
        githubService = Client(context, isTesting = true)
            .createGithubService(mockServer.url("/"))
    }

    @After
    fun onFinish() {
        mockServer.shutdown()
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun test_GetUserList() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .addHeader("link: <https://api.github.com/users?since=46>; rel=\"next\", <https://api.github.com/users{?since}>; rel=\"first\"")
            .setBody(
                "[\n" +
                        "  {\n" +
                        "    \"login\": \"octocat\",\n" +
                        "    \"id\": 1,\n" +
                        "    \"node_id\": \"MDQ6VXNlcjE=\",\n" +
                        "    \"avatar_url\": \"https://github.com/images/error/octocat_happy.gif\",\n" +
                        "    \"gravatar_id\": \"\",\n" +
                        "    \"url\": \"https://api.github.com/users/octocat\",\n" +
                        "    \"html_url\": \"https://github.com/octocat\",\n" +
                        "    \"followers_url\": \"https://api.github.com/users/octocat/followers\",\n" +
                        "    \"following_url\": \"https://api.github.com/users/octocat/following{/other_user}\",\n" +
                        "    \"gists_url\": \"https://api.github.com/users/octocat/gists{/gist_id}\",\n" +
                        "    \"starred_url\": \"https://api.github.com/users/octocat/starred{/owner}{/repo}\",\n" +
                        "    \"subscriptions_url\": \"https://api.github.com/users/octocat/subscriptions\",\n" +
                        "    \"organizations_url\": \"https://api.github.com/users/octocat/orgs\",\n" +
                        "    \"repos_url\": \"https://api.github.com/users/octocat/repos\",\n" +
                        "    \"events_url\": \"https://api.github.com/users/octocat/events{/privacy}\",\n" +
                        "    \"received_events_url\": \"https://api.github.com/users/octocat/received_events\",\n" +
                        "    \"type\": \"User\",\n" +
                        "    \"site_admin\": false\n" +
                        "  }\n" +
                        "]"
            )

        mockServer.enqueue(mockResponse)

        githubService.getUsers(0).parse(
            onError = { error -> assertNull(error) },
            onSuccess = { headers, response ->

                val offset = RemoteHelpers.getNextOffsetOfUsers(headers)

                assertEquals(46, offset)
                assertEquals(1, response.size)
                assertEquals("octocat", response[0].login)
            }
        )
    }

    @Test
    fun test_GetUserInfo() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                "{\n" +
                        "  \"login\": \"octocat\",\n" +
                        "  \"id\": 1,\n" +
                        "  \"node_id\": \"MDQ6VXNlcjE=\",\n" +
                        "  \"avatar_url\": \"https://github.com/images/error/octocat_happy.gif\",\n" +
                        "  \"gravatar_id\": \"\",\n" +
                        "  \"url\": \"https://api.github.com/users/octocat\",\n" +
                        "  \"html_url\": \"https://github.com/octocat\",\n" +
                        "  \"followers_url\": \"https://api.github.com/users/octocat/followers\",\n" +
                        "  \"following_url\": \"https://api.github.com/users/octocat/following{/other_user}\",\n" +
                        "  \"gists_url\": \"https://api.github.com/users/octocat/gists{/gist_id}\",\n" +
                        "  \"starred_url\": \"https://api.github.com/users/octocat/starred{/owner}{/repo}\",\n" +
                        "  \"subscriptions_url\": \"https://api.github.com/users/octocat/subscriptions\",\n" +
                        "  \"organizations_url\": \"https://api.github.com/users/octocat/orgs\",\n" +
                        "  \"repos_url\": \"https://api.github.com/users/octocat/repos\",\n" +
                        "  \"events_url\": \"https://api.github.com/users/octocat/events{/privacy}\",\n" +
                        "  \"received_events_url\": \"https://api.github.com/users/octocat/received_events\",\n" +
                        "  \"type\": \"User\",\n" +
                        "  \"site_admin\": false,\n" +
                        "  \"name\": \"monalisa octocat\",\n" +
                        "  \"company\": \"GitHub\",\n" +
                        "  \"blog\": \"https://github.com/blog\",\n" +
                        "  \"location\": \"San Francisco\",\n" +
                        "  \"email\": \"octocat@github.com\",\n" +
                        "  \"hireable\": false,\n" +
                        "  \"bio\": \"There once was...\",\n" +
                        "  \"twitter_username\": \"monatheoctocat\",\n" +
                        "  \"public_repos\": 2,\n" +
                        "  \"public_gists\": 1,\n" +
                        "  \"followers\": 20,\n" +
                        "  \"following\": 0,\n" +
                        "  \"created_at\": \"2008-01-14T04:33:35Z\",\n" +
                        "  \"updated_at\": \"2008-01-14T04:33:35Z\"\n" +
                        "}"
            )

        mockServer.enqueue(mockResponse)

        githubService.getUser("octocat").parse(
            onError = { error -> assertNull("An error occurred", error) },
            onSuccess = { headers, model ->

                assertEquals("octocat", model.login)
                assertEquals(1, model.id)
                assertEquals("https://github.com/octocat", model.htmlUrl)
            }
        )
    }

}