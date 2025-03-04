package com.example.flux.di

import android.content.Context
import com.example.flux.preferences.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
open class LocalServiceModule {

    @Provides
    fun provideUserPreferences(
        @ApplicationContext context: Context,
    ): UserPreferences {
        return UserPreferences.getInstance(
            context,
        )
    }
}
