package dev.hirogakatageri.sandbox.data

import android.content.Context
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dev.hirogakatageri.sandbox.data.service.ApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

class ApiClient(private val context: Context) {

    private val client: OkHttpClient by lazy { createClient() }

    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().setLevel(BODY)
    }

    private val retrofit: Retrofit by lazy { setupRetrofit() }

    private fun createClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        val cache = Cache(
            File(context.cacheDir, "http_cache"),
            50L * 1024L * 1024L
        )

        builder.addInterceptor(loggingInterceptor)
        builder.cache(cache)

        return builder.build()
    }

    private fun setupRetrofit(): Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create())

        return builder.build()
    }

    fun createApiService() = retrofit.create(ApiService::class.java)

    companion object {
        const val BASE_URL = "https://y25a93i78c.execute-api.ap-southeast-1.amazonaws.com/"
    }
}
