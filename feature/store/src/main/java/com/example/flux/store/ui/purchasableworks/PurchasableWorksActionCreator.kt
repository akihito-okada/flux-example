package com.example.flux.store.ui.purchasableworks

import androidx.lifecycle.Lifecycle
import com.example.flux.common.flux.actions.GlobalActions
import com.example.flux.common.delegate.ErrorHandlerDelegate
import com.example.flux.common.delegate.GlobalActionDelegate
import com.example.flux.common.flux.Dispatcher
import com.example.flux.common.flux.util.TaggedDispatcherHandler
import com.example.flux.common.util.lifecycle.LifecycleCoroutineScope.coroutineScope
import com.example.flux.common.model.LoadingState
import com.example.flux.model.PurchasableWorksSortType
import com.example.flux.model.StoreSearchConditions
import com.example.flux.model.TagId
import com.example.flux.model.ToySaveRequest
import com.example.flux.model.analytics.ScrollPosition
import com.example.flux.repository.ToysRepository
import com.example.flux.repository.UserRepository
import com.example.flux.repository.util.ext.JobExt.cancelIfNeeded
import com.example.flux.store.actions.StoreActions
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PurchasableWorksActionCreator @AssistedInject constructor(
    @Assisted lifecycle: Lifecycle,
    @Assisted override var tagId: TagId,
    override val dispatcher: Dispatcher,
    private val toysRepository: ToysRepository,
    private val userRepository: UserRepository,
    errorHandlerDelegate: ErrorHandlerDelegate,
) : CoroutineScope by lifecycle.coroutineScope,
    ErrorHandlerDelegate by errorHandlerDelegate,
    GlobalActionDelegate,
    TaggedDispatcherHandler {

    private var loadWorksJob: Job? = null

    fun loadPurchasableWorks(
        conditions: StoreSearchConditions,
        conditionsOld: StoreSearchConditions = conditions,
        offset: Int = 0,
    ) {
        if (loadWorksJob != null) {
            loadWorksJob?.cancelIfNeeded()
            loadWorksJob = null
        }
        loadWorksJob = launch(
            CoroutineExceptionHandler { _, exception ->
                onError(exception)
                launchAndDispatch(StoreActions.StoreHomeSearchConditionsUpdated(conditionsOld))
                launchAndDispatch(StoreActions.PurchasableWorksAllItemsLoaded(false))
                launchAndDispatch(StoreActions.PurchasableWorksLoadingStateChanged(LoadingState.Loaded))
            },
        ) {
            dispatch(StoreActions.PurchasableWorksLoadingStateChanged(LoadingState.Loading))
            dispatch(StoreActions.StoreHomeSearchConditionsUpdated(conditions))
            if (!userRepository.hasUserId()) {
                userRepository.createUser()
            }
            val feedItems = toysRepository.searchToys(conditions)
            dispatch(StoreActions.PurchasableWorksAllItemsLoaded(true))
            dispatch(StoreActions.PurchasableWorksUpdated(feedItems, offset == 0))
            dispatch(StoreActions.PurchasableWorksLoadingStateChanged(LoadingState.Loaded))
        }
    }

    fun loadSortType(sortType: PurchasableWorksSortType) {
        launchAndDispatch(StoreActions.PurchasableWorksSortTypeChanged(sortType))
    }

    fun loadCurrentScrollPosition(scrollPosition: ScrollPosition) {
        launchAndDispatch(StoreActions.PurchasableWorksScrollPositionChanged(scrollPosition))
    }

    fun keepWork(toySaveRequest: ToySaveRequest) {
        launch(
            CoroutineExceptionHandler { _, exception ->
                onError(exception)
                launchAndDispatch(StoreActions.PurchasableWorksLoadingStateChanged(LoadingState.Loaded))
            },
        ) {
            dispatch(StoreActions.PurchasableWorksLoadingStateChanged(LoadingState.Loading))
            when {
                toySaveRequest.hasSaved -> toysRepository.saveToy(toySaveRequest)
                else -> toysRepository.removeSavedToy(toySaveRequest)
            }
            dispatch(GlobalActions.WorkKept(toySaveRequest))
            dispatch(StoreActions.PurchasableWorksLoadingStateChanged(LoadingState.Loaded))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            lifecycle: Lifecycle,
            tagId: TagId,
        ): PurchasableWorksActionCreator
    }
}
