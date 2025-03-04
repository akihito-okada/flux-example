package com.example.flux.store.ui.searchconditiondetail

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.flux.common.flux.Dispatcher
import com.example.flux.common.flux.Store
import com.example.flux.common.util.ext.StateFlowExt.combine
import com.example.flux.common.model.LoadingState
import com.example.flux.common.model.SnackbarMessage
import com.example.flux.model.StoreSearchConditions
import com.example.flux.model.TagId
import com.example.flux.model.ToyCategory
import com.example.flux.model.ToyColor
import com.example.flux.model.ToyMaterial
import com.example.flux.model.ToysCount
import com.example.flux.store.actions.StoreActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Immutable
@HiltViewModel
class SearchConditionDetailBottomSheetDialogStore
@Inject
constructor(
    val dispatcher: Dispatcher,
    private val savedStateHandle: SavedStateHandle,
) : Store() {

    private val tagId: TagId by lazy { TagId.fromSavedStateHandle(savedStateHandle) }
    private val initialConditions: StoreSearchConditions by lazy { StoreSearchConditions.fromSavedStateHandle(savedStateHandle) }
    val conditions get() = searchConditionsLoaded.value

    private val snackbarMessageRequest: StateFlow<SnackbarMessage> = dispatcher
        .subscribe<StoreActions.SearchConditionBottomSheetSnackbarMessageRequest>()
        .filter { it.tagId == tagId }
        .map { it.snackbarMessageRequest }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SnackbarMessage(),
        )

    private val searchConditionsLoaded: StateFlow<StoreSearchConditions> = dispatcher
        .subscribe<StoreActions.SearchConditionBottomSheetSearchConditionsUpdated>()
        .filter { it.tagId == tagId }
        .map { it.condition }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = initialConditions,
        )

    private val toysCountLoaded: StateFlow<ToysCount> = dispatcher
        .subscribe<StoreActions.SearchConditionBottomSheetSearchedCountLoaded>()
        .filter { it.tagId == tagId }
        .map { it.toysCount }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ToysCount(),
        )

    private val materialsTechniquesLoaded: StateFlow<List<ToyMaterial>> = dispatcher
        .subscribe<StoreActions.SearchConditionBottomSheetMaterialsTechniquesLoaded>()
        .filter { it.tagId == tagId }
        .map { it.materialsTechniques }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = listOf(),
        )

    private val categoriesLoaded: StateFlow<List<ToyCategory>> = dispatcher
        .subscribe<StoreActions.SearchConditionBottomSheetCategoriesLoaded>()
        .filter { it.tagId == tagId }
        .map { it.categories }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = listOf(),
        )

    private val colorCategoriesLoaded: StateFlow<List<ToyColor>> = dispatcher
        .subscribe<StoreActions.SearchConditionBottomSheetColorCategoriesLoaded>()
        .filter { it.tagId == tagId }
        .map { it.colorCategories }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = listOf(),
        )

    private val allItemsLoaded: StateFlow<Boolean> = dispatcher
        .subscribe<StoreActions.SearchConditionBottomSheetAllItemsLoaded>()
        .filter { it.tagId == tagId }
        .map { it.isLoaded }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false,
        )

    private val loadingState = dispatcher
        .subscribe<StoreActions.SearchConditionBottomSheetLoadingStateChanged>()
        .filter { it.tagId == tagId }
        .map { it.loadingState }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = LoadingState.Loading,
        )

    val screenContent = combine(
        searchConditionsLoaded,
        categoriesLoaded,
        materialsTechniquesLoaded,
        colorCategoriesLoaded,
        allItemsLoaded,
        toysCountLoaded,
        loadingState,
        snackbarMessageRequest,
    ) {
            searchConditions,
            categories,
            materialsTechniques,
            colorCategories,
            allItemsLoaded,
            worksLoaded,
            loadingState,
            snackBarMessage,
        ->
        SearchConditionDetailScreenContent(
            searchConditions,
            categories,
            materialsTechniques,
            colorCategories,
            allItemsLoaded,
            worksLoaded,
            loadingState,
            snackBarMessage,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SearchConditionDetailScreenContent(),
    )
}
