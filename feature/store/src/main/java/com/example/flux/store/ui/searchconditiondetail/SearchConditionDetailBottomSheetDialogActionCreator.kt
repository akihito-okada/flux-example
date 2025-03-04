package com.example.flux.store.ui.searchconditiondetail

import androidx.compose.runtime.Immutable
import androidx.lifecycle.Lifecycle
import com.example.flux.common.delegate.ErrorHandlerDelegate
import com.example.flux.common.delegate.ShowMessageDelegate
import com.example.flux.common.flux.Dispatcher
import com.example.flux.common.flux.util.TaggedDispatcherHandler
import com.example.flux.common.util.lifecycle.LifecycleCoroutineScope.coroutineScope
import com.example.flux.common.model.LoadingState
import com.example.flux.common.model.SnackbarMessage
import com.example.flux.model.StoreSearchConditions
import com.example.flux.model.TagId
import com.example.flux.repository.ToysRepository
import com.example.flux.repository.util.ext.JobExt.cancelIfNeeded
import com.example.flux.store.actions.StoreActions
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import timber.log.Timber

@Immutable
class SearchConditionDetailBottomSheetDialogActionCreator @AssistedInject constructor(
    @Assisted lifecycle: Lifecycle,
    @Assisted override var tagId: TagId,
    override val dispatcher: Dispatcher,
    private val toysRepository: ToysRepository,
    showMessageDelegate: ShowMessageDelegate,
    errorHandlerDelegate: ErrorHandlerDelegate,
) : CoroutineScope by lifecycle.coroutineScope,
    ShowMessageDelegate by showMessageDelegate,
    ErrorHandlerDelegate by errorHandlerDelegate,
    TaggedDispatcherHandler {

    private var loadMastersJob: Job? = null
    private var searchWorksJob: Job? = null

    fun updateSearchCondition(
        conditions: StoreSearchConditions,
        conditionsOld: StoreSearchConditions = conditions,
    ) {
        var isInitial = true
        if (searchWorksJob != null) {
            searchWorksJob?.cancelIfNeeded()
            searchWorksJob = null
            isInitial = false
        }
        searchWorksJob = launch(
            CoroutineExceptionHandler { _, exception ->
                onError(exception) {
                    launchAndDispatch(StoreActions.SearchConditionBottomSheetSnackbarMessageRequest(SnackbarMessage(it)))
                }
                launchAndDispatch(StoreActions.SearchConditionBottomSheetSearchConditionsUpdated(conditionsOld))
                launchAndDispatch(StoreActions.SearchConditionBottomSheetLoadingStateChanged(
                    LoadingState.Error))
            },
        ) {
            dispatch(StoreActions.SearchConditionBottomSheetLoadingStateChanged(LoadingState.Loading))
            dispatch(StoreActions.SearchConditionBottomSheetSearchConditionsUpdated(conditions))
            if (isInitial.not()) {
                delay(DELAY_CONTINUOUS_UPDATING_SEARCH_CONDITION_MS)
            }
            val toysCount = toysRepository.searchToysCount(conditions)
            dispatch(StoreActions.SearchConditionBottomSheetSearchedCountLoaded(toysCount))
            dispatch(StoreActions.SearchConditionBottomSheetLoadingStateChanged(LoadingState.Loaded))
        }
    }

    fun loadWorkMasters() {
        if (loadMastersJob != null) {
            loadMastersJob?.cancelIfNeeded()
            loadMastersJob = null
        }
        loadMastersJob = launch(
            CoroutineExceptionHandler { _, exception ->
                onError(exception) {
                    launchAndDispatch(StoreActions.SearchConditionBottomSheetSnackbarMessageRequest(SnackbarMessage(it)))
                }
                launchAndDispatch(StoreActions.SearchConditionBottomSheetAllItemsLoaded(false))
                launchAndDispatch(StoreActions.SearchConditionBottomSheetLoadingStateChanged(
                    LoadingState.Error))
            },
        ) {
            supervisorScope {
                dispatch(StoreActions.SearchConditionBottomSheetLoadingStateChanged(LoadingState.Loading))
                val loadPurchasableWorkCategories = async { toysRepository.getCategories() }
                val loadPurchasableWorkMaterialsTechniques = async { toysRepository.getMaterials() }
                val loadPurchasableWorkColorCategories = async { toysRepository.getColors() }
                val categories = runCatching { loadPurchasableWorkCategories.await() }
                val materialsTechniques = runCatching { loadPurchasableWorkMaterialsTechniques.await() }
                val colorCategories = runCatching { loadPurchasableWorkColorCategories.await() }
                var hasError = false
                when {
                    categories.isSuccess -> {
                        categories.getOrNull().also {
                            dispatch(StoreActions.SearchConditionBottomSheetCategoriesLoaded(it ?: listOf()))
                        }
                    }

                    categories.isFailure -> {
                        Timber.d(categories.exceptionOrNull())
                        hasError = true
                    }
                }
                when {
                    materialsTechniques.isSuccess -> {
                        materialsTechniques.getOrNull()?.also {
                            dispatch(StoreActions.SearchConditionBottomSheetMaterialsTechniquesLoaded(it))
                        }
                    }

                    materialsTechniques.isFailure -> {
                        Timber.d(materialsTechniques.exceptionOrNull())
                        hasError = true
                    }
                }
                when {
                    colorCategories.isSuccess -> {
                        colorCategories.getOrNull()?.also {
                            dispatch(StoreActions.SearchConditionBottomSheetColorCategoriesLoaded(it))
                        }
                    }

                    colorCategories.isFailure -> {
                        Timber.d(colorCategories.exceptionOrNull())
                        hasError = true
                    }
                }
                dispatch(StoreActions.SearchConditionBottomSheetAllItemsLoaded(hasError.not()))
                dispatch(
                    StoreActions.SearchConditionBottomSheetLoadingStateChanged(
                        when {
                            hasError -> LoadingState.Error
                            else -> LoadingState.Loaded
                        },
                    ),
                )
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            lifecycle: Lifecycle,
            tagId: TagId,
        ): SearchConditionDetailBottomSheetDialogActionCreator
    }

    companion object {

        private const val DELAY_CONTINUOUS_UPDATING_SEARCH_CONDITION_MS = 150L
    }
}
