package com.hirogakatageri.remote

import android.content.Context
import com.hirogakatageri.remote.call.NetworkResponseAdapterFactory
import com.hirogakatageri.remote.service.MainService
import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import net.gotev.cookiestore.SharedPreferencesCookieStore
import net.gotev.cookiestore.WebKitSyncCookieManager
import net.gotev.cookiestore.syncToWebKitCookieManager
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit


internal const val TIMEOUT_CONNECT: Long = 60
internal const val TIMEOUT_READ: Long = 60

class Client(
    applicationContext: Context,
    private val isTesting: Boolean = false
) {

    private val cacheDirectory = applicationContext.cacheDir
    private val chuckInterceptor by lazy { ChuckInterceptor(applicationContext) }
    private val cookieStore by lazy { SharedPreferencesCookieStore(applicationContext, "cookies") }

    fun createService(): MainService = Retrofit.Builder()
        .baseUrl("https://api.github.com")
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
        .create(MainService::class.java)

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

            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })

                if (!isTesting) addInterceptor(chuckInterceptor)
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

            var requestCount = 1
            val maxRetryCount = 3

            while (response.code != 200 && requestCount < maxRetryCount) {

                Thread.sleep(1000)
                response = chain.proceed(request)
                requestCount++
            }

            return response
        }
    }

}