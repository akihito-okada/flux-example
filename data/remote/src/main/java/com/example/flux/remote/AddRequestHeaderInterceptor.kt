package com.example.flux.remote

import com.example.flux.model.util.EnvironmentConfig
import com.example.flux.remote.util.addHeaders
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

class AddRequestHeaderInterceptor(
    private val environmentConfig: EnvironmentConfig,
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        return chain.proceed(requestBuilder.addHeaders(environmentConfig).build())
    }
}
