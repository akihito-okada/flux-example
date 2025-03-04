package com.example.flux.works.ui.toydetail

import androidx.lifecycle.Lifecycle
import com.example.flux.common.flux.actions.GlobalActions
import com.example.flux.common.delegate.ErrorHandlerDelegate
import com.example.flux.common.delegate.GlobalActionDelegate
import com.example.flux.common.flux.Dispatcher
import com.example.flux.common.flux.util.TaggedDispatcherHandler
import com.example.flux.common.util.lifecycle.LifecycleCoroutineScope.coroutineScope
import com.example.flux.common.util.navigator.Navigator
import com.example.flux.common.model.LoadingState
import com.example.flux.model.TagId
import com.example.flux.model.ToyId
import com.example.flux.model.ToySaveRequest
import com.example.flux.repository.ToysRepository
import com.example.flux.works.actions.ToysActions
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ToyDetailActionCreator @AssistedInject constructor(
    @Assisted lifecycle: Lifecycle,
    @Assisted override var tagId: TagId,
    @Assisted val toyId: ToyId,
    override val dispatcher: Dispatcher,
    private val toysRepository: ToysRepository,
    errorHandlerDelegate: ErrorHandlerDelegate,
    navigator: Navigator,
) : CoroutineScope by lifecycle.coroutineScope,
    ErrorHandlerDelegate by errorHandlerDelegate,
    Navigator by navigator,
    TaggedDispatcherHandler,
    GlobalActionDelegate {

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        onError(exception) {
        }
        launchAndDispatch(ToysActions.WorkDetailAboutLoadingStateChanged(LoadingState.Loaded))
    }

    private val exceptionHandlerForList = CoroutineExceptionHandler { _, exception ->
        onError(exception) {
        }
        launchAndDispatch(ToysActions.WorkDetailAboutLoadingStateChanged(LoadingState.Loaded))
    }

    fun loadWorkDetailAbout() {
        launch(exceptionHandlerForList) {
            dispatch(ToysActions.WorkDetailAboutLoadingStateChanged(LoadingState.Loading))
            dispatch(ToysActions.ToyDetailUpdated(toysRepository.toyDetails(toyId)))
            dispatch(ToysActions.WorkDetailAboutLoadingStateChanged(LoadingState.Loaded))
        }
    }

    fun keepWork(toySaveRequest: ToySaveRequest) {
        launch(exceptionHandler) {
            dispatch(ToysActions.WorkDetailAboutLoadingStateChanged(LoadingState.Loading))
            when {
                toySaveRequest.hasSaved -> toysRepository.saveToy(toySaveRequest)
                else -> toysRepository.removeSavedToy(toySaveRequest)
            }
            dispatch(GlobalActions.WorkKept(toySaveRequest))
            dispatch(ToysActions.WorkDetailAboutLoadingStateChanged(LoadingState.Loaded))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            lifecycle: Lifecycle,
            tagId: TagId,
            toyId: ToyId,
        ): ToyDetailActionCreator
    }
}
