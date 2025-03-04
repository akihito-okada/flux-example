package com.example.flux.ui.main

import androidx.lifecycle.Lifecycle
import com.example.flux.common.flux.actions.CommonActions
import com.example.flux.common.flux.actions.GlobalActions
import com.example.flux.common.delegate.ErrorHandlerDelegate
import com.example.flux.common.delegate.GlobalActionDelegate
import com.example.flux.common.delegate.SystemDelegate
import com.example.flux.common.flux.Dispatcher
import com.example.flux.common.flux.util.DispatcherHandler
import com.example.flux.common.util.lifecycle.LifecycleCoroutineScope.coroutineScope
import com.example.flux.repository.NetworkStateRepository
import com.example.flux.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActionCreator @AssistedInject constructor(
    override val dispatcher: Dispatcher,
    @Assisted lifecycle: Lifecycle,
    private val userRepository: UserRepository,
    private val networkStateRepository: NetworkStateRepository,
    errorHandlerDelegate: ErrorHandlerDelegate,
) : CoroutineScope by lifecycle.coroutineScope,
    ErrorHandlerDelegate by errorHandlerDelegate,
    DispatcherHandler,
    SystemDelegate,
    GlobalActionDelegate {

    init {
        observeNetworkState()
    }

    fun loadShouldShowRecomposeHighlighter() {
        launchAndDispatch(GlobalActions.ShouldShowRecomposeHighlighterLoaded(userRepository.loadShouldShowRecomposeHighlighter()))
    }

    fun initialize() {
        launch(
            CoroutineExceptionHandler { _, exception ->
                Timber.d(exception)
            },
        ) {
            dispatch(CommonActions.MainSplashStateChanged(true))
            val hasUserId = userRepository.hasUserId()
            if (!hasUserId) {
                userRepository.createUser()
            }
            dispatch(CommonActions.MainSplashStateChanged(false))
            dispatch(CommonActions.MainHomeInitializeNeeded)
        }
    }

    private fun observeNetworkState() {
        launch(ignoreExceptionHandler) {
            val flow = networkStateRepository.watchNetwork()
            flow.collect {
                dispatch(GlobalActions.IsNetworkAvailableUpdated(it))
            }
        }
    }

    fun keepAppTheme() {
        launchAndDispatch(GlobalActions.SystemThemeChanged(userRepository.loadAppTheme()))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            lifecycle: Lifecycle,
        ): MainActionCreator
    }
}
