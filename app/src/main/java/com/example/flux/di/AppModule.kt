package com.example.flux.di

import android.content.Context
import com.example.flux.model.util.EnvironmentConfig
import com.example.flux.model.util.ResourceProvider
import com.example.flux.remote.NetworkWatcher
import com.example.flux.util.EnvironmentConfigImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideCoroutineScope() = CoroutineScope(SupervisorJob())

    @Provides
    fun provideEnvironmentConfig(@ApplicationContext context: Context): EnvironmentConfig {
        return EnvironmentConfigImpl(context)
    }

    @Provides
    fun provideResourceProvider(@ApplicationContext context: Context): ResourceProvider {
        return ResourceProvider.getInstance(context)
    }

    @Provides
    fun provideNetworkWatcher(@ApplicationContext context: Context): NetworkWatcher {
        return NetworkWatcher(context)
    }
}
