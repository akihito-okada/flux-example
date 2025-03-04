package com.example.flux.store.ui.searchconditiondetail

import com.example.flux.common.model.LoadingState
import com.example.flux.common.model.SnackbarMessage
import com.example.flux.model.StoreSearchConditions
import com.example.flux.model.ToyCategory
import com.example.flux.model.ToyColor
import com.example.flux.model.ToyMaterial
import com.example.flux.model.ToysCount
import com.example.flux.model.WorkProductionYearsType

data class SearchConditionDetailScreenContent(
    val conditions: StoreSearchConditions = StoreSearchConditions(),
    val categories: List<ToyCategory> = listOf(),
    val materialsTechniques: List<ToyMaterial> = listOf(),
    val colorCategories: List<ToyColor> = listOf(),
    val allItemsLoaded: Boolean = false,
    val toysCount: ToysCount = ToysCount(),
    val loadingState: LoadingState = LoadingState.Loaded,
    val snackBarMessage: SnackbarMessage = SnackbarMessage(),
) {

    val items: List<SearchConditionFeedItem>
        get() {
            val list = mutableListOf<SearchConditionFeedItem>()
            // カテゴリ
            if (categories.isNotEmpty()) {
                list.add(
                    SearchConditionFeedItem.SearchWorkConditionCategoryHeader(
                        selectedCount = conditions.selectedCategories.size,
                        isExpanded = conditions.isExpandedCategoriesHeader,
                    ),
                )
                if (conditions.isExpandedCategoriesHeader) {
                    categories.forEachIndexed { index, category ->
                        if (conditions.isExpandedCategories.not() &&
                            MAX_SIZE_COLLAPSED_LIST <= index
                        ) {
                            return@forEachIndexed
                        }
                        list.add(
                            SearchConditionFeedItem.SearchWorkConditionCategoryItem(
                                category = category,
                                isSelected = conditions.selectedCategories.contains(category),
                            ),
                        )
                    }
                    list.add(SearchConditionFeedItem.SearchWorkConditionCategoryShowAll(conditions.isExpandedCategories))
                    list.add(SearchConditionFeedItem.SearchWorkConditionCategoryDivider)
                }
            }
            // 価格
            list.add(
                SearchConditionFeedItem.SearchWorkConditionPriceHeader(
                    selectedCount = conditions.selectedCountPrice,
                    isExpanded = conditions.isExpandedPricesHeader,
                ),
            )
            if (conditions.isExpandedPricesHeader) {
                list.add(SearchConditionFeedItem.SearchWorkConditionPriceRangeItem(conditions.selectedPriceRange))
                list.add(SearchConditionFeedItem.SearchWorkConditionPriceDivider)
            }

            // 素材・技法
            if (materialsTechniques.isNotEmpty()) {
                list.add(
                    SearchConditionFeedItem.SearchWorkConditionMaterialsTechniqueHeader(
                        selectedCount = conditions.selectedMaterialsTechniques.size,
                        isExpanded = conditions.isExpandedMaterialsTechniquesHeader,
                    ),
                )
                if (conditions.isExpandedMaterialsTechniquesHeader) {
                    materialsTechniques.forEachIndexed { index, materialsTechnique ->
                        if (conditions.isExpandedMaterialsTechniques.not() &&
                            MAX_SIZE_COLLAPSED_LIST <= index
                        ) {
                            return@forEachIndexed
                        }
                        list.add(
                            SearchConditionFeedItem.SearchWorkConditionMaterialsTechniqueItem(
                                materialsTechnique = materialsTechnique,
                                isSelected = conditions.selectedMaterialsTechniques.contains(materialsTechnique),
                            ),
                        )
                    }
                    list.add(SearchConditionFeedItem.SearchWorkConditionMaterialsTechniqueShowAll(conditions.isExpandedMaterialsTechniques))
                    list.add(SearchConditionFeedItem.SearchWorkConditionMaterialsTechniqueDivider)
                }
            }
            // 色カテゴリ
            if (colorCategories.isNotEmpty()) {
                list.add(
                    SearchConditionFeedItem.SearchWorkConditionColorCategoryHeader(
                        selectedCount = conditions.selectedColorCategories.size,
                        isExpanded = conditions.isExpandedColorCategoriesHeader,
                    ),
                )
                if (conditions.isExpandedColorCategoriesHeader) {
                    colorCategories.forEachIndexed { index, colorCategory ->
                        if (conditions.isExpandedColorCategories.not() &&
                            MAX_SIZE_COLLAPSED_LIST <= index
                        ) {
                            return@forEachIndexed
                        }
                        list.add(
                            SearchConditionFeedItem.SearchWorkConditionColorCategoryItem(
                                colorCategory = colorCategory,
                                isSelected = conditions.selectedColorCategories.contains(colorCategory),
                            ),
                        )
                    }
                    list.add(SearchConditionFeedItem.SearchWorkConditionColorCategoryShowAll(conditions.isExpandedColorCategories))
                    list.add(SearchConditionFeedItem.SearchWorkConditionColorCategoryDivider)
                }
            }
            // 製作年
            list.add(
                SearchConditionFeedItem.SearchWorkConditionProductionYearsTypeHeader(
                    selectedCount = conditions.selectedProductionYearsTypes.size,
                    isExpanded = conditions.isExpandedProductionYearsHeader,
                ),
            )
            if (conditions.isExpandedProductionYearsHeader) {
                WorkProductionYearsType.items.forEachIndexed { index, type ->
                    if (conditions.isExpandedProductionYears.not() &&
                        MAX_SIZE_COLLAPSED_LIST <= index
                    ) {
                        return@forEachIndexed
                    }
                    list.add(
                        SearchConditionFeedItem.SearchWorkConditionProductionYearsTypeItem(
                            yearsType = type,
                            isSelected = conditions.selectedProductionYearsTypes.contains(type),
                        ),
                    )
                }
                list.add(SearchConditionFeedItem.SearchWorkConditionProductionYearsTypeShowAll(conditions.isExpandedProductionYears))
                list.add(SearchConditionFeedItem.SearchWorkConditionProductionYearsTypeDivider)
            }

            return list
        }

    companion object {
        private const val MAX_SIZE_COLLAPSED_LIST = 4
    }
}
