package com.example.flux.model

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoreSearchConditions(
    val selectedToyIds: List<ToyId> = listOf(),
    val selectedSortType: PurchasableWorksSortType = PurchasableWorksSortType.default,
    val selectedCategories: List<ToyCategory> = listOf(),
    val selectedPriceRange: PriceRange = PriceRange(),
    val selectedMaterialsTechniques: List<ToyMaterial> = listOf(),
    val selectedColorCategories: List<ToyColor> = listOf(),
    val selectedProductionYearsTypes: List<WorkProductionYearsType> = listOf(),
    // For BottomSheet
    val isExpandedCategories: Boolean = false,
    val isExpandedMaterialsTechniques: Boolean = false,
    val isExpandedColorCategories: Boolean = false,
    val isExpandedProductionYears: Boolean = false,
    val isExpandedCategoriesHeader: Boolean = false,
    val isExpandedPricesHeader: Boolean = false,
    val isExpandedMaterialsTechniquesHeader: Boolean = false,
    val isExpandedColorCategoriesHeader: Boolean = false,
    val isExpandedProductionYearsHeader: Boolean = false,
    val isExpandedOtherOptionsHeader: Boolean = false,
    val isClickedStickyHeader: Boolean = false,
) : Parcelable {

    @IgnoredOnParcel
    val toyIdsParameter = selectedToyIds.takeIf { it.isNotEmpty() }
        ?.map { it.value.toInt() }

    @IgnoredOnParcel
    val maxPrice = selectedPriceRange.maxPrice

    @IgnoredOnParcel
    val minPrice = selectedPriceRange.minPrice

    @IgnoredOnParcel
    val materialsTechniquesParameter = selectedMaterialsTechniques.takeIf { it.isNotEmpty() }
        ?.map { it.id.value.toInt() }

    @IgnoredOnParcel
    val colorCategoriesParameter = selectedColorCategories.takeIf { it.isNotEmpty() }
        ?.map { it.id.value.toInt() }

    @IgnoredOnParcel
    val productionYearsTypesParameter = selectedProductionYearsTypes.takeIf { it.isNotEmpty() }
        ?.flatMap { it.searchConditions }
        ?.distinct()

    @IgnoredOnParcel
    val categoryIdsParameter = selectedCategories.takeIf { it.isNotEmpty() }
        ?.map { it.id.value.toInt() }

    val selectedCountPrice: Int
        get() {
            val numPriceRange = if (selectedPriceRange.isDefault.not()) 1 else 0
            return numPriceRange
        }

    val isInitialCondition: Boolean
        get() = selectedCategories.isEmpty() &&
            selectedMaterialsTechniques.isEmpty() &&
            selectedColorCategories.isEmpty() &&
            selectedProductionYearsTypes.isEmpty() &&
            minPrice.isRangeMin &&
            maxPrice.isRangeMax

    fun getChipsItems(): List<PurchasableWorksChipsItem> {
        val categoriesSorted = selectedCategories.sortedByDescending { it.id.value }
        val materialsTechniquesSorted = selectedMaterialsTechniques.sortedByDescending { it.id.value }
        val colorCategoriesSorted = selectedColorCategories.sortedByDescending { it.id.value }
        val productionYearsTypesSorted = selectedProductionYearsTypes.sortedBy { it.ordinal }
        val items = mutableListOf<PurchasableWorksChipsItem>()
        // カテゴリ
        categoriesSorted.forEach {
            items.add(PurchasableWorksChipsItem.CategoryItem(it))
        }
        // 価格
        if (selectedPriceRange.isDefault.not()) {
            items.add(PurchasableWorksChipsItem.PriceRangeItem(selectedPriceRange))
        }
        // 素材・技法
        materialsTechniquesSorted.forEach {
            items.add(PurchasableWorksChipsItem.MaterialsTechniqueItem(it))
        }
        // 色カテゴリ
        colorCategoriesSorted.forEach {
            items.add(PurchasableWorksChipsItem.ColorCategoryItem(it))
        }
        // 製作年
        productionYearsTypesSorted.forEach {
            items.add(PurchasableWorksChipsItem.ProductionYearsTypeItem(it))
        }
        if (isInitialCondition.not()) {
            items.add(PurchasableWorksChipsItem.ClearAllItem)
        }
        return items
    }

    fun toArguments(arguments: Bundle) {
        arguments.putParcelable(ARG_STORE_SEARCH_CONDITIONS, this)
    }

    companion object {
        private const val ARG_STORE_SEARCH_CONDITIONS = "store_search_conditions"

        fun fromSavedStateHandle(
            savedStateHandle: SavedStateHandle,
        ): StoreSearchConditions {
            return savedStateHandle.get<StoreSearchConditions>(ARG_STORE_SEARCH_CONDITIONS) ?: StoreSearchConditions()
        }

        fun fromArguments(arguments: Bundle?): StoreSearchConditions {
            return arguments?.getParcelable(ARG_STORE_SEARCH_CONDITIONS) as? StoreSearchConditions
                ?: StoreSearchConditions()
        }
    }
}
