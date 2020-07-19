package com.hirogakatageri.network

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.readystatesoftware.chuck.ChuckInterceptor
import net.gotev.cookiestore.SharedPreferencesCookieStore
import net.gotev.cookiestore.WebKitSyncCookieManager
import net.gotev.cookiestore.syncToWebKitCookieManager
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit

private const val HEAD_AUTHORIZATION = "Authorization"
private const val HEAD_TOKEN = "Token"
private const val TIMEOUT_CONNECT: Long = 60
private const val TIMEOUT_READ: Long = 60

class Client(applicationContext: Context) {

    private val cacheDirectory = applicationContext.cacheDir
    private val chuckInterceptor by lazy { ChuckInterceptor(applicationContext) }
    private val cookieStore by lazy { SharedPreferencesCookieStore(applicationContext, "cookies") }
    private val securedNetworkPreferences by lazy {
        SecuredNetworkPreferences(
            applicationContext,
            "network_preferences",
            "master"
        )
    }

    private fun createOkHttpClient() {

        val cache = cacheDirectory?.let { Cache(it, 8 * 1024 * 1024) }

        OkHttpClient.Builder().run {
            cache(cache)
            connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            cookieJar(JavaNetCookieJar(createCookieHandler()))
            addInterceptor(RetryInterceptor())
            addInterceptor(RefreshTokenInterceptor())
            addInterceptor(AuthInterceptor())

            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                addInterceptor(chuckInterceptor)
            }

        }
    }

    private fun createCookieHandler() =
        WebKitSyncCookieManager(cookieStore, CookiePolicy.ACCEPT_ALL).apply {
            cookieStore.syncToWebKitCookieManager()
        }.also { CookieManager.setDefault(it) }

    private inner class RefreshTokenInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()

            return chain.proceed(request).let {
                when (it.code) {
                    490, 400 -> refreshToken(it.request, chain)
                    else -> it
                }
            }
        }

        private fun refreshToken(request: Request, chain: Interceptor.Chain): Response {
            FirebaseAuth.getInstance().currentUser?.let {
                it.getIdToken(true).addOnSuccessListener { result ->
                    securedNetworkPreferences.accessToken = result?.token
                }
            }

            Thread.sleep(1000)

            return chain.proceed(request)
        }

    }

    private inner class AuthInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
                .newBuilder()
                //.addHeader(AUTHORIZATION, BuildConfig.HEADER_AUTH)
                .addHeader(HEAD_TOKEN, securedNetworkPreferences.accessToken ?: "")
                .build()

            return chain.proceed(request)
        }
    }

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