package com.example.flux.di

import com.example.flux.model.util.EnvironmentConfig
import com.example.flux.remote.NetworkManager
import com.example.flux.remote.ToysFactoryApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class RemoteServiceModule {

    @Singleton
    @Provides
    fun provideNetworkManager(
        environmentConfig: EnvironmentConfig,
    ): NetworkManager {
        return NetworkManager(environmentConfig)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(networkManager: NetworkManager): OkHttpClient {
        return networkManager.getHttpClient()
    }

    @Singleton
    @Provides
    fun provideToysFactoryApiService(networkManager: NetworkManager): ToysFactoryApiService {
        return networkManager.buildToysFactoryApiService()
    }
}
