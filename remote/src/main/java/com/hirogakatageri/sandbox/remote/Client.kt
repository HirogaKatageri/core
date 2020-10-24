package com.hirogakatageri.sandbox.remote

import android.content.Context
import com.hirogakatageri.sandbox.remote.call.NetworkResponseAdapterFactory
import com.hirogakatageri.sandbox.remote.service.GithubService
import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import net.gotev.cookiestore.SharedPreferencesCookieStore
import net.gotev.cookiestore.WebKitSyncCookieManager
import net.gotev.cookiestore.syncToWebKitCookieManager
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit
import kotlin.math.pow


internal const val TIMEOUT_CONNECT: Long = 60
internal const val TIMEOUT_READ: Long = 60

class Client(
    applicationContext: Context,
    private val isTesting: Boolean = false
) {

    private val cacheDirectory = applicationContext.cacheDir
    private val chuckInterceptor by lazy { ChuckInterceptor(applicationContext) }
    private val cookieStore by lazy { SharedPreferencesCookieStore(applicationContext, "cookies") }

    fun createGithubService(baseUrl: HttpUrl = "https://api.github.com".toHttpUrl()): GithubService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(createOkHttpClient())
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            )
        )
        .build()
        .create(GithubService::class.java)

    private fun createOkHttpClient(): OkHttpClient {

        val cache = cacheDirectory?.let { Cache(it, 8 * 1024 * 1024) }

        return OkHttpClient.Builder().run {
            cache(cache)
            connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)

            if (!isTesting) {
                cookieJar(JavaNetCookieJar(createCookieHandler()))
                addInterceptor(RetryInterceptor())
            }

            if (BuildConfig.DEBUG && !isTesting) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })

                addInterceptor(chuckInterceptor)
            }
            build()
        }
    }

    private fun createCookieHandler() =
        WebKitSyncCookieManager(cookieStore, CookiePolicy.ACCEPT_ALL).apply {
            cookieStore.syncToWebKitCookieManager()
        }.also { CookieManager.setDefault(it) }

    private class RetryInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            var response = chain.proceed(request)

            val seconds = 2L..5L // To increment exponential backoff
            var retries = 0
            val maxRetries = 3

            while (response.code != 200 && retries < maxRetries) {

                ++retries
                val delay: Long = (((3.25).pow(retries) + seconds.random()) * 1000L).toLong()

                Thread.sleep(delay)
                response = chain.proceed(request)
            }

            return response
        }
    }

}