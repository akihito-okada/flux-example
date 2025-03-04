package com.example.flux.di

import com.example.flux.preferences.UserPreferences
import com.example.flux.remote.NetworkWatcher
import com.example.flux.remote.ToysFactoryApiService
import com.example.flux.repository.NetworkStateRepository
import com.example.flux.repository.ToysRepository
import com.example.flux.repository.UserRepository
import com.example.flux.repository.impl.NetworkStateRepositoryImpl
import com.example.flux.repository.impl.ToysRepositoryImpl
import com.example.flux.repository.impl.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
open class RepositoryModule {

    @Provides
    fun provideUserRepository(
        toysFactoryApiService: ToysFactoryApiService,
        userPreferences: UserPreferences,
    ): UserRepository {
        return UserRepositoryImpl(
            toysFactoryApiService = toysFactoryApiService,
            userPreferences = userPreferences,
        )
    }

    @Provides
    fun provideToysRepository(
        toysFactoryApiService: ToysFactoryApiService,
        userPreferences: UserPreferences,
    ): ToysRepository {
        return ToysRepositoryImpl(
            toysFactoryApiService = toysFactoryApiService,
            userPreferences = userPreferences,
        )
    }

    @Provides
    fun provideNetworkStateRepository(networkWatcher: NetworkWatcher): NetworkStateRepository {
        return NetworkStateRepositoryImpl(networkWatcher)
    }
}
