package com.example.flux.di

import com.example.flux.common.delegate.ErrorHandlerDelegate
import com.example.flux.common.delegate.ErrorHandlerDelegateImpl
import com.example.flux.common.delegate.ShowMessageDelegate
import com.example.flux.common.delegate.ShowMessageDelegateImpl
import com.example.flux.common.flux.Dispatcher
import com.example.flux.model.util.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
open class DelegateModule {
    @Provides
    fun provideShowMessageDelegate(dispatcher: Dispatcher, resourceProvider: ResourceProvider): ShowMessageDelegate {
        return ShowMessageDelegateImpl(dispatcher, resourceProvider)
    }

    @Provides
    fun provideErrorHandlerDelegate(dispatcher: Dispatcher, resourceProvider: ResourceProvider, showMessageDelegate: ShowMessageDelegate): ErrorHandlerDelegate {
        return ErrorHandlerDelegateImpl(dispatcher, resourceProvider, showMessageDelegate)
    }
}
