package com.example.flux.di

import android.app.Activity
import com.example.flux.common.util.navigator.Navigator
import com.example.flux.model.util.EnvironmentConfig
import com.example.flux.util.NavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    @ActivityScoped
    fun provideNavigator(activity: Activity, environmentConfig: EnvironmentConfig): Navigator {
        return NavigatorImpl(activity, environmentConfig)
    }
}
