package com.example.flux.works.ui.toydetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.flux.common.flux.actions.GlobalActions
import com.example.flux.common.flux.Dispatcher
import com.example.flux.common.flux.Store
import com.example.flux.common.model.LoadingState
import com.example.flux.model.TagId
import com.example.flux.model.Toy
import com.example.flux.model.ToySaveRequest
import com.example.flux.works.actions.ToysActions
import com.example.flux.works.ui.toydetail.compose.ToyDetailScreenContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ToyDetailStore
@Inject
constructor(
    val dispatcher: Dispatcher,
    private val savedStateHandle: SavedStateHandle,
) : Store() {

    private val tagId: TagId by lazy { TagId.fromSavedStateHandle(savedStateHandle) }

    private val loadingState: StateFlow<LoadingState> = dispatcher
        .subscribe<ToysActions.WorkDetailAboutLoadingStateChanged>()
        .filter { it.tagId == tagId }
        .map { it.loadingState }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = LoadingState.Loading,
        )

    private val toyDetailUpdated: StateFlow<Toy> = dispatcher
        .subscribe<ToysActions.ToyDetailUpdated>()
        .filter { it.tagId == tagId }
        .map { it.toy }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Toy(),
        )

    private val toySaveRequested = dispatcher
        .subscribe<GlobalActions.WorkKept>()
        .map { action ->
            toyDetailUpdated.value.hasSaved = action.toySaveRequest.hasSaved
            action.toySaveRequest
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ToySaveRequest(),
        )

    val screenContent = combine(
        loadingState,
        toyDetailUpdated,
        toySaveRequested,
    ) {
            loadingState,
            toy,
            toySaveRequest,
        ->
        ToyDetailScreenContent(
            toy = toy,
            loadingState = loadingState,
            toySaveRequest = toySaveRequest,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ToyDetailScreenContent(),
    )
}
