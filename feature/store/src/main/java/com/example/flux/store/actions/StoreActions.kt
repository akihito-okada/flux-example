package com.example.flux.store.actions

import com.example.flux.common.flux.Action
import com.example.flux.common.model.LoadingState
import com.example.flux.model.PurchasableWorksSortType
import com.example.flux.common.model.SnackbarMessage
import com.example.flux.model.StoreSearchConditions
import com.example.flux.model.TagId
import com.example.flux.model.Toy
import com.example.flux.model.ToyCategory
import com.example.flux.model.ToyColor
import com.example.flux.model.ToyMaterial
import com.example.flux.model.ToysCount
import com.example.flux.model.analytics.ScrollPosition

sealed class StoreActions : Action {

    override var tagId = TagId()

    // StoreHome
    class StoreHomeSearchConditionsUpdated(val condition: StoreSearchConditions) : StoreActions()

    // PurchasableWorks
    class PurchasableWorksLoadingStateChanged(val loadingState: LoadingState) : StoreActions()
    class PurchasableWorksUpdated(val storeWorks: List<Toy>, val isInitial: Boolean) : StoreActions()
    class PurchasableWorksAllItemsLoaded(val isLoaded: Boolean) : StoreActions()
    class PurchasableWorksSortTypeChanged(val sortType: PurchasableWorksSortType) : StoreActions()
    class PurchasableWorksScrollPositionChanged(val scrollPosition: ScrollPosition) : StoreActions()

    // SearchConditionBottomSheet
    class SearchConditionBottomSheetLoadingStateChanged(val loadingState: LoadingState) : StoreActions()
    class SearchConditionBottomSheetSearchConditionsUpdated(val condition: StoreSearchConditions) : StoreActions()
    class SearchConditionBottomSheetSearchedCountLoaded(val toysCount: ToysCount) : StoreActions()
    class SearchConditionBottomSheetCategoriesLoaded(val categories: List<ToyCategory>) : StoreActions()
    class SearchConditionBottomSheetMaterialsTechniquesLoaded(val materialsTechniques: List<ToyMaterial>) : StoreActions()
    class SearchConditionBottomSheetColorCategoriesLoaded(val colorCategories: List<ToyColor>) : StoreActions()
    class SearchConditionBottomSheetAllItemsLoaded(val isLoaded: Boolean) : StoreActions()
    class SearchConditionBottomSheetSnackbarMessageRequest(val snackbarMessageRequest: SnackbarMessage) : StoreActions()
}
