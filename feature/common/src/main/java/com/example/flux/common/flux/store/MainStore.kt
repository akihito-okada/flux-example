package com.example.flux.common.flux.store

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.flux.common.flux.actions.CommonActions
import com.example.flux.common.flux.actions.GlobalActions
import com.example.flux.common.flux.Dispatcher
import com.example.flux.common.flux.Store
import com.example.flux.common.util.lifecycle.Event
import com.example.flux.model.AppThemeType
import com.example.flux.common.model.FabState
import com.example.flux.common.model.MainScreenContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Immutable
@HiltViewModel
class MainStore @Inject constructor(
    dispatcher: Dispatcher,
) : Store() {

    val isNetworkAvailable get() = networkStateUpdated.value ?: false

    val initializeNeeded: LiveData<Event<Unit>> = dispatcher
        .subscribe<CommonActions.MainHomeInitializeNeeded>()
        .map { }
        .toLiveDataWithEvent(this, null)

    val showSplashNeeded: LiveData<Event<Boolean?>> = dispatcher
        .subscribe<CommonActions.MainSplashStateChanged>()
        .map { it.shouldShow }
        .toLiveDataWithEvent(this, null)

    val searchFabStateChanged: LiveData<FabState> = dispatcher
        .subscribe<GlobalActions.StoreFabStateChanged>()
        .map { it.state }
        .toLiveData(this, FabState.Expanded)

    private val networkStateUpdated: LiveData<Boolean> = dispatcher
        .subscribe<GlobalActions.IsNetworkAvailableUpdated>()
        .map { it.isAvailable }
        .toLiveData(this, null)

    val systemThemeChanged: LiveData<Event<AppThemeType>> = dispatcher
        .subscribe<GlobalActions.SystemThemeChanged>()
        .map { it.currentAppTheme }
        .toLiveDataWithEvent(this, null)

    private val isNetworkAvailableUpdated: StateFlow<Boolean> = dispatcher
        .subscribe<GlobalActions.IsNetworkAvailableUpdated>()
        .map { it.isAvailable }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false,
        )

    private val shouldShowRecomposeHighlighterLoaded: StateFlow<Boolean> = dispatcher
        .subscribe<GlobalActions.ShouldShowRecomposeHighlighterLoaded>()
        .map { it.shouldShow }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false,
        )

    val screenContent = combine(
        isNetworkAvailableUpdated,
        shouldShowRecomposeHighlighterLoaded,
    ) {
            isNetworkAvailable,
            shouldShowRecomposeHighlighter,
        ->
        MainScreenContent(
            isNetworkAvailable = isNetworkAvailable,
            shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MainScreenContent(),
    )
}
