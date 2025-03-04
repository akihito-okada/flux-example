package com.example.flux.remote

import com.example.flux.model.util.EnvironmentConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT_TIME: Long = 30
private const val READ_TIMEOUT_TIME: Long = 30
private const val WRITE_TIMEOUT_TIME: Long = 30

class NetworkManager(
    private val environmentConfig: EnvironmentConfig,
) {

    private val canDebug = environmentConfig.isDevelopment || environmentConfig.isStaging || environmentConfig.isDebug
    private val okHttpClientForApi = getHttpClientForApi()

    fun buildToysFactoryApiService(): ToysFactoryApiService {
        return Retrofit.Builder()
            .baseUrl(environmentConfig.apiHostVX)
            .addConverterFactory(GsonConverterFactory.create(GsonFactory.create()))
            .client(okHttpClientForApi).build()
            .create(ToysFactoryApiService::class.java)
    }

    /**
     * For WebView, Glide
     */
    fun getHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(CONNECTION_TIMEOUT_TIME, TimeUnit.SECONDS)
        builder.readTimeout(READ_TIMEOUT_TIME, TimeUnit.SECONDS)
        builder.writeTimeout(WRITE_TIMEOUT_TIME, TimeUnit.SECONDS)
        builder.addInterceptor(AddRequestHeaderInterceptor(environmentConfig))
        if (canDebug) {
            builder.addInterceptor(
                HttpLoggingInterceptor().also {
                    it.level = HttpLoggingInterceptor.Level.BODY
                },
            )
        }
        return builder.build()
    }

    private fun getHttpClientForApi(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(CONNECTION_TIMEOUT_TIME, TimeUnit.SECONDS)
        builder.readTimeout(READ_TIMEOUT_TIME, TimeUnit.SECONDS)
        builder.writeTimeout(WRITE_TIMEOUT_TIME, TimeUnit.SECONDS)
        builder.addInterceptor(ApiAddRequestHeaderInterceptor(environmentConfig))
        builder.addInterceptor(ResponseInterceptor())
        if (canDebug) {
            builder.addInterceptor(
                HttpLoggingInterceptor().also {
                    it.level = HttpLoggingInterceptor.Level.BODY
                },
            )
        }
        return builder.build()
    }
}
