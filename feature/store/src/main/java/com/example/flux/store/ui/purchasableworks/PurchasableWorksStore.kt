package com.example.flux.store.ui.purchasableworks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.example.flux.common.flux.actions.GlobalActions
import com.example.flux.common.flux.Dispatcher
import com.example.flux.common.flux.Store
import com.example.flux.common.util.lifecycle.Event
import com.example.flux.model.BaseItem
import com.example.flux.common.model.FeedItem
import com.example.flux.common.model.FeedItemsUpdateRequest
import com.example.flux.common.model.LoadingState
import com.example.flux.model.PurchasableWorksSortType
import com.example.flux.model.StoreSearchConditions
import com.example.flux.model.TagId
import com.example.flux.model.Toy
import com.example.flux.model.ToySaveRequest
import com.example.flux.model.analytics.ScrollPosition
import com.example.flux.store.actions.StoreActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PurchasableWorksStore
@Inject
constructor(
    val dispatcher: Dispatcher,
    private val savedStateHandle: SavedStateHandle,
) : Store() {

    private val tagId: TagId by lazy { TagId.fromSavedStateHandle(savedStateHandle) }

    private val feedItems: MutableList<BaseItem> = arrayListOf()
    val sortType get() = sortTypeChanged.value ?: PurchasableWorksSortType.default
    val conditions get() = searchConditionsUpdated.value ?: StoreSearchConditions()
    var numTotalWorks = 0
    val isNotInitialized get() = purchasableWorksUpdated.value == null
    val scrollPosition get() = scrollPositionChanged.value ?: ScrollPosition()

    val loadingState: LiveData<Event<LoadingState>> = dispatcher
        .subscribe<StoreActions.PurchasableWorksLoadingStateChanged>()
        .filter { it.tagId == tagId }
        .map { it.loadingState }
        .toLiveDataWithEvent(this, null)

    private val purchasableWorksLoaded: LiveData<FeedItemsUpdateRequest> = dispatcher
        .subscribe<StoreActions.PurchasableWorksUpdated>()
        .filter { it.tagId == tagId }
        .map {
            if (it.isInitial) {
                feedItems.clear()
            }
            feedItems.addAll(it.storeWorks)
            numTotalWorks = it.storeWorks.size
            FeedItemsUpdateRequest(feedItems, it.isInitial)
        }
        .toLiveData(this, null)

    val searchConditionsUpdated: LiveData<StoreSearchConditions> = dispatcher
        .subscribe<StoreActions.StoreHomeSearchConditionsUpdated>()
        .filter { it.tagId == tagId }
        .map { it.condition }
        .toLiveData(this, null)

    val sortTypeChanged: LiveData<PurchasableWorksSortType> = dispatcher
        .subscribe<StoreActions.PurchasableWorksSortTypeChanged>()
        .filter { it.tagId == tagId }
        .map { it.sortType }
        .toLiveData(this, null)

    private val scrollPositionChanged: LiveData<ScrollPosition> = dispatcher
        .subscribe<StoreActions.PurchasableWorksScrollPositionChanged>()
        .filter { it.tagId == tagId }
        .map { it.scrollPosition }
        .toLiveData(this, null)

    val isAllItemLoaded: LiveData<Boolean> = dispatcher
        .subscribe<StoreActions.PurchasableWorksAllItemsLoaded>()
        .filter { it.tagId == tagId }
        .map { it.isLoaded }
        .toLiveData(this, null)

    private val toySaved: LiveData<Event<ToySaveRequest>> = dispatcher
        .subscribe<GlobalActions.WorkKept>()
        .map { action ->
            // アイテムを更新してから feedItems を更新
            val items = feedItems.map { item ->
                when {
                    item is Toy && item.id == action.toySaveRequest.toy.id ->
                        item.copy(hasSaved = action.toySaveRequest.hasSaved)

                    else -> item
                }
            }
            feedItems.clear()
            feedItems.addAll(items)
            action.toySaveRequest
        }
        .toLiveDataWithEvent(this, null)

    val purchasableWorksUpdated = MediatorLiveData<FeedItemsUpdateRequest>().also { liveData ->
        val observer = Observer<Any> {
            val activities = purchasableWorksLoaded.value ?: return@Observer
            val items = arrayListOf<BaseItem>()
            if (feedItems.isNotEmpty()) {
                items.addAll(feedItems)
            } else {
                items.add(FeedItem.EmptyItem())
            }
            liveData.postValue(FeedItemsUpdateRequest(items, activities.isInitial))
        }
        liveData.addSource(purchasableWorksLoaded, observer)
        liveData.addSource(toySaved, observer)
    }
}
