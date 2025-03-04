package com.example.flux.store.ui.searchconditiondetail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flux.common.compose.component.LoadingView
import com.example.flux.common.compose.theme.AppTheme
import com.example.flux.common.flux.store.MainStore
import com.example.flux.model.PriceRange
import com.example.flux.model.StoreSearchConditions
import com.example.flux.store.ui.searchconditiondetail.SearchConditionFeedItem
import com.example.flux.store.ui.searchconditiondetail.SearchConditionDetailBottomSheetDialogActionCreator
import com.example.flux.store.ui.searchconditiondetail.SearchConditionDetailBottomSheetDialogStore
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.random.Random

@Composable
fun SearchConditionScreen(
    actionCreator: SearchConditionDetailBottomSheetDialogActionCreator,
    store: SearchConditionDetailBottomSheetDialogStore,
    mainStore: MainStore,
    onClickClose: () -> Unit = {},
    onClickSearch: (StoreSearchConditions) -> Unit = {},
    onScrollState: (isTop: Boolean) -> Unit,
) {
    val screenContent by store.screenContent.collectAsState()
    val mainScreenContent by mainStore.screenContent.collectAsState()
    val items = screenContent.items
    val isAllItemsLoaded by remember {
        derivedStateOf { screenContent.allItemsLoaded }
    }
    val conditions by remember {
        derivedStateOf { screenContent.conditions }
    }
    val listState = rememberLazyListState()
    val loadingState by remember {
        derivedStateOf { screenContent.loadingState }
    }
    val toysCount by remember {
        derivedStateOf { screenContent.toysCount }
    }
    val shouldShowRecomposeHighlighter by remember {
        derivedStateOf {
            mainScreenContent.shouldShowRecomposeHighlighter
        }
    }
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.Transparent,
        topBar = {
            Column {
                SearchConditionDetailHeader(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                        ),
                    shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                    onClickClose = remember {
                        {
                            onClickClose.invoke()
                        }
                    },
                )
                Divider(
                    color = AppTheme.colors.borderSoft,
                )
            }
        },
        bottomBar = {
            Column {
                Divider(
                    color = AppTheme.colors.borderSoft,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
                SearchConditionDetailFooter(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                        ),
                    shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                    itemCount = toysCount.count,
                    onClickClear = remember {
                        {
                            actionCreator.updateSearchCondition(
                                StoreSearchConditions(
                                    selectedSortType = conditions.selectedSortType,
                                    selectedPriceRange = PriceRange(revision = Random.nextInt()),
                                ),
                            )
                        }
                    },
                    onClickSearch = remember {
                        {
                            onClickSearch.invoke(conditions)
                        }
                    },
                )
            }
        },
    ) { paddingValues ->
        when {
            isAllItemsLoaded -> LazyColumn(
                modifier = Modifier.padding(paddingValues),
                state = listState,
            ) {
                items.forEachIndexed { index, item ->
                    when (item) {
                        // カテゴリ
                        is SearchConditionFeedItem.SearchWorkConditionCategoryHeader -> stickyHeader(key = item.type, contentType = this) {
                            SearchConditionHeaderCard(
                                modifier = Modifier.animateItemPlacement(),
                                shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                                count = conditions.selectedCategories.size,
                                isExpanded = item.isExpanded,
                                titleRes = item.titleRes,
                                onClick = remember {
                                    {
                                        actionCreator.updateSearchCondition(
                                            conditions.copy(
                                                isExpandedCategoriesHeader = it,
                                                isClickedStickyHeader = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index == index,
                                            ),
                                        )
                                    }
                                },
                            )
                        }

                        is SearchConditionFeedItem.SearchWorkConditionCategoryItem -> item(key = item.type) {
                            SearchConditionCheckboxItemCard(
                                modifier = Modifier.animateItemPlacement(),
                                shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                                title = item.category.name,
                                isSelected = item.isSelected,
                                onChecked = remember {
                                    { isSelected ->
                                        val category = item.category
                                        val selectedCategories = conditions.selectedCategories.toMutableList().also {
                                            it.remove(category)
                                            if (isSelected) {
                                                it.add(category)
                                            }
                                        }
                                        actionCreator.updateSearchCondition(conditions.copy(selectedCategories = selectedCategories))
                                    }
                                },
                            )
                        }

                        is SearchConditionFeedItem.SearchWorkConditionCategoryShowAll -> item(key = item.type) {
                            SearchConditionDetailShowAll(
                                modifier = Modifier.animateItemPlacement(),
                                shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                                isExpanded = item.isExpanded,
                                onClick = remember {
                                    {
                                        actionCreator.updateSearchCondition(conditions.copy(isExpandedCategories = it))
                                    }
                                },
                            )
                        }
                        // 価格
                        is SearchConditionFeedItem.SearchWorkConditionPriceHeader -> stickyHeader(key = item.type, contentType = this) {
                            SearchConditionHeaderCard(
                                modifier = Modifier.animateItemPlacement(),
                                shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                                isExpanded = item.isExpanded,
                                count = item.selectedCount,
                                titleRes = item.titleRes,
                                onClick = remember {
                                    {
                                        actionCreator.updateSearchCondition(
                                            conditions.copy(
                                                isExpandedPricesHeader = it,
                                                isClickedStickyHeader = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index == index,
                                            ),
                                        )
                                    }
                                },
                            )
                        }

                        is SearchConditionFeedItem.SearchWorkConditionPriceRangeItem -> item(key = item.type) {
                            SearchConditionPriceRangeSliderCard(
                                modifier = Modifier.animateItemPlacement(),
                                shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                                selectedPriceRange = item.priceRange,
                                onValueChangeFinished = remember {
                                    { priceRange ->
                                        actionCreator.updateSearchCondition(
                                            conditions.copy(selectedPriceRange = priceRange),
                                        )
                                    }
                                },
                            )
                        }
                        // 技法・素材
                        is SearchConditionFeedItem.SearchWorkConditionMaterialsTechniqueHeader -> stickyHeader(key = item.type, contentType = this) {
                            SearchConditionHeaderCard(
                                modifier = Modifier.animateItemPlacement(),
                                shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                                isExpanded = item.isExpanded,
                                count = item.selectedCount,
                                titleRes = item.titleRes,
                                onClick = remember {
                                    {
                                        actionCreator.updateSearchCondition(
                                            conditions.copy(
                                                isExpandedMaterialsTechniquesHeader = it,
                                                isClickedStickyHeader = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index == index,
                                            ),
                                        )
                                    }
                                },
                            )
                        }

                        is SearchConditionFeedItem.SearchWorkConditionMaterialsTechniqueItem -> item(key = item.type) {
                            SearchConditionCheckboxItemCard(
                                modifier = Modifier.animateItemPlacement(),
                                shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                                title = item.materialsTechnique.name,
                                isSelected = item.isSelected,
                                onChecked = remember {
                                    { isSelected ->
                                        val materialsTechnique = item.materialsTechnique
                                        val selectedMaterialsTechniques = conditions.selectedMaterialsTechniques.toMutableList().also {
                                            it.remove(materialsTechnique)
                                            if (isSelected) {
                                                it.add(materialsTechnique)
                                            }
                                        }
                                        actionCreator.updateSearchCondition(conditions.copy(selectedMaterialsTechniques = selectedMaterialsTechniques))
                                    }
                                },
                            )
                        }

                        is SearchConditionFeedItem.SearchWorkConditionMaterialsTechniqueShowAll -> item(key = item.type) {
                            SearchConditionDetailShowAll(
                                modifier = Modifier.animateItemPlacement(),
                                shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                                isExpanded = item.isExpanded,
                                onClick = remember {
                                    {
                                        actionCreator.updateSearchCondition(conditions.copy(isExpandedMaterialsTechniques = it))
                                    }
                                },
                            )
                        }
                        // 色カテゴリ
                        is SearchConditionFeedItem.SearchWorkConditionColorCategoryHeader -> stickyHeader(key = item.type, contentType = this) {
                            SearchConditionHeaderCard(
                                modifier = Modifier.animateItemPlacement(),
                                shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                                isExpanded = item.isExpanded,
                                count = item.selectedCount,
                                titleRes = item.titleRes,
                                onClick = remember {
                                    {
                                        actionCreator.updateSearchCondition(
                                            conditions.copy(
                                                isExpandedColorCategoriesHeader = it,
                                                isClickedStickyHeader = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index == index,
                                            ),
                                        )
                                    }
                                },
                            )
                        }

                        is SearchConditionFeedItem.SearchWorkConditionColorCategoryItem -> item(key = item.type) {
                            SearchConditionCheckboxItemCard(
                                modifier = Modifier.animateItemPlacement(),
                                shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                                title = item.colorCategory.name,
                                isSelected = item.isSelected,
                                onChecked = remember {
                                    { isSelected ->
                                        val colorCategory = item.colorCategory
                                        val selectedColorCategories = conditions.selectedColorCategories.toMutableList().also {
                                            it.remove(colorCategory)
                                            if (isSelected) {
                                                it.add(colorCategory)
                                            }
                                        }
                                        actionCreator.updateSearchCondition(conditions.copy(selectedColorCategories = selectedColorCategories))
                                    }
                                },
                            )
                        }

                        is SearchConditionFeedItem.SearchWorkConditionColorCategoryShowAll -> item(key = item.type) {
                            SearchConditionDetailShowAll(
                                modifier = Modifier.animateItemPlacement(),
                                shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                                isExpanded = item.isExpanded,
                                onClick = remember {
                                    {
                                        actionCreator.updateSearchCondition(conditions.copy(isExpandedColorCategories = it))
                                    }
                                },
                            )
                        }

                        is SearchConditionFeedItem.SearchWorkConditionProductionYearsTypeHeader -> stickyHeader(key = item.type, contentType = this) {
                            SearchConditionHeaderCard(
                                modifier = Modifier.animateItemPlacement(),
                                shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                                isExpanded = item.isExpanded,
                                count = item.selectedCount,
                                titleRes = item.titleRes,
                                onClick = remember {
                                    {
                                        actionCreator.updateSearchCondition(
                                            conditions.copy(
                                                isExpandedProductionYearsHeader = it,
                                                isClickedStickyHeader = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index == index,
                                            ),
                                        )
                                    }
                                },
                            )
                        }

                        is SearchConditionFeedItem.SearchWorkConditionProductionYearsTypeItem -> item(key = item.type) {
                            SearchConditionCheckboxItemCard(
                                modifier = Modifier.animateItemPlacement(),
                                shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                                title = stringResource(item.yearsType.labelRes),
                                isSelected = item.isSelected,
                                onChecked = remember {
                                    { isSelected ->
                                        val yearsType = item.yearsType
                                        val selectedProductionYearsType = conditions.selectedProductionYearsTypes.toMutableList().also {
                                            it.remove(yearsType)
                                            if (isSelected) {
                                                it.add(yearsType)
                                            }
                                        }
                                        actionCreator.updateSearchCondition(conditions.copy(selectedProductionYearsTypes = selectedProductionYearsType))
                                    }
                                },
                            )
                        }

                        is SearchConditionFeedItem.SearchWorkConditionProductionYearsTypeShowAll -> item(key = item.type) {
                            SearchConditionDetailShowAll(
                                modifier = Modifier.animateItemPlacement(),
                                shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                                isExpanded = item.isExpanded,
                                onClick = remember {
                                    {
                                        actionCreator.updateSearchCondition(conditions.copy(isExpandedProductionYears = it))
                                    }
                                },
                            )
                        }
                        // Divider
                        is SearchConditionFeedItem.SearchWorkConditionCategoryDivider,
                        is SearchConditionFeedItem.SearchWorkConditionPriceDivider,
                        is SearchConditionFeedItem.SearchWorkConditionColorCategoryDivider,
                        is SearchConditionFeedItem.SearchWorkConditionMaterialsTechniqueDivider,
                        is SearchConditionFeedItem.SearchWorkConditionProductionYearsTypeDivider,
                            -> item(key = item.type) {
                            SearchConditionDividerCard(
                                modifier = Modifier.animateItemPlacement(),
                                shouldShowRecomposeHighlighter = shouldShowRecomposeHighlighter,
                            )
                        }
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier
                            .animateItemPlacement()
                            .height(150.dp),
                    )
                }
            }

            else -> if (loadingState.isLoading) {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center,
                ) {
                    LoadingView(
                        modifier = Modifier
                            .wrapContentSize(),
                    )
                }
            }
        }
    }
    LaunchedEffect(isAllItemsLoaded.not()) {
        if (isAllItemsLoaded.not()) {
            actionCreator.loadWorkMasters()
            actionCreator.updateSearchCondition(conditions)
        }
    }
    val snackBarMessage by remember {
        derivedStateOf { screenContent.snackBarMessage }
    }
    if (loadingState.hasError) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            if (snackBarMessage.isValid) {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = snackBarMessage.message,
                )
            }
        }
    }
    LaunchedEffect(
        key1 = listState,
        key2 = items,
        key3 = isAllItemsLoaded,
    ) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }
            .map {
                // リストの先頭かどうか判定
                it == 0 || isAllItemsLoaded.not()
            }
            .distinctUntilChanged()
            .collect {
                onScrollState(it)
            }
    }
    // 少なく表示するときのスクロール位置の保持
    // Categories
    ObserveCollapsedState(
        isExpandedTarget = conditions.isExpandedCategories,
    ) {
        listState.scrollInnerContentToIfNeeded(
            listState.firstVisibleItemIndex,
            items.indexOf(
                items.firstOrNull {
                    it is SearchConditionFeedItem.SearchWorkConditionCategoryHeader
                },
            ),
        )
    }
    ObserveCollapsedState(
        isExpandedTarget = conditions.isExpandedCategoriesHeader,
    ) {
        if (conditions.isClickedStickyHeader) {
            listState.scrollHeaderToIfNeeded(
                items.indexOf(
                    items.firstOrNull {
                        it is SearchConditionFeedItem.SearchWorkConditionCategoryHeader
                    },
                ),
            )
            actionCreator.updateSearchCondition(conditions = conditions.copy(isClickedStickyHeader = false))
        }
    }
    // ProductionYears
    ObserveCollapsedState(
        isExpandedTarget = conditions.isExpandedProductionYears,
    ) {
        listState.scrollInnerContentToIfNeeded(
            listState.firstVisibleItemIndex,
            items.indexOf(
                items.firstOrNull {
                    it is SearchConditionFeedItem.SearchWorkConditionProductionYearsTypeHeader
                },
            ),
        )
    }
    ObserveCollapsedState(
        isExpandedTarget = conditions.isExpandedProductionYearsHeader,
    ) {
        if (conditions.isClickedStickyHeader) {
            listState.scrollHeaderToIfNeeded(
                items.indexOf(
                    items.firstOrNull {
                        it is SearchConditionFeedItem.SearchWorkConditionProductionYearsTypeHeader
                    },
                ),
            )
            actionCreator.updateSearchCondition(conditions = conditions.copy(isClickedStickyHeader = false))
        }
    }
    // ColorCategories
    ObserveCollapsedState(
        isExpandedTarget = conditions.isExpandedColorCategories,
    ) {
        listState.scrollInnerContentToIfNeeded(
            listState.firstVisibleItemIndex,
            items.indexOf(
                items.firstOrNull {
                    it is SearchConditionFeedItem.SearchWorkConditionColorCategoryHeader
                },
            ),
        )
    }
    ObserveCollapsedState(
        isExpandedTarget = conditions.isExpandedColorCategoriesHeader,
    ) {
        if (conditions.isClickedStickyHeader) {
            listState.scrollHeaderToIfNeeded(
                items.indexOf(
                    items.firstOrNull {
                        it is SearchConditionFeedItem.SearchWorkConditionColorCategoryHeader
                    },
                ),
            )
            actionCreator.updateSearchCondition(conditions = conditions.copy(isClickedStickyHeader = false))
        }
    }
    // MaterialsTechniques
    ObserveCollapsedState(
        isExpandedTarget = conditions.isExpandedMaterialsTechniques,
    ) {
        listState.scrollInnerContentToIfNeeded(
            listState.firstVisibleItemIndex,
            items.indexOf(
                items.firstOrNull {
                    it is SearchConditionFeedItem.SearchWorkConditionMaterialsTechniqueHeader
                },
            ),
        )
    }
    ObserveCollapsedState(
        isExpandedTarget = conditions.isExpandedMaterialsTechniquesHeader,
    ) {
        if (conditions.isClickedStickyHeader) {
            listState.scrollHeaderToIfNeeded(
                items.indexOf(
                    items.firstOrNull {
                        it is SearchConditionFeedItem.SearchWorkConditionMaterialsTechniqueHeader
                    },
                ),
            )
            actionCreator.updateSearchCondition(conditions = conditions.copy(isClickedStickyHeader = false))
        }
    }
    // Prices
    ObserveCollapsedState(
        isExpandedTarget = conditions.isExpandedPricesHeader,
    ) {
        if (conditions.isClickedStickyHeader) {
            listState.scrollHeaderToIfNeeded(
                items.indexOf(
                    items.firstOrNull {
                        it is SearchConditionFeedItem.SearchWorkConditionPriceHeader
                    },
                ),
            )
            actionCreator.updateSearchCondition(conditions = conditions.copy(isClickedStickyHeader = false))
        }
    }
}

private suspend fun LazyListState.scrollInnerContentToIfNeeded(firstVisibleIndex: Int, index: Int, offset: Int = 0) {
    if (index < 0) {
        return
    }
    if (firstVisibleIndex == 0) {
        return
    }
    animateScrollToItem(index = index, scrollOffset = offset)
}

private suspend fun LazyListState.scrollHeaderToIfNeeded(index: Int, offset: Int = 0) {
    if (index < 0) {
        return
    }
    animateScrollToItem(index = index, scrollOffset = offset)
}
