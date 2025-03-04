package com.example.flux.store.ui.searchconditiondetail

import androidx.annotation.StringRes
import com.example.flux.model.PriceRange
import com.example.flux.model.R
import com.example.flux.model.ToyCategory
import com.example.flux.model.ToyColor
import com.example.flux.model.ToyMaterial
import com.example.flux.model.WorkProductionYearsType

sealed class SearchConditionFeedItem(typeId: Long = 0) {

    val type = "${this::class.simpleName}$typeId"

    // カテゴリ
    object SearchWorkConditionCategoryDivider : SearchConditionFeedItem()

    data class SearchWorkConditionCategoryHeader(
        @StringRes val titleRes: Int = R.string.filter_title_categories,
        val selectedCount: Int,
        val isExpanded: Boolean,
        val shouldShowDivider: Boolean = false,
    ) : SearchConditionFeedItem()

    data class SearchWorkConditionCategoryItem(
        val category: ToyCategory,
        val isSelected: Boolean,
    ) : SearchConditionFeedItem(category.id.value)

    data class SearchWorkConditionCategoryShowAll(val isExpanded: Boolean) : SearchConditionFeedItem()

    // 価格
    object SearchWorkConditionPriceDivider : SearchConditionFeedItem()

    data class SearchWorkConditionPriceHeader(
        @StringRes val titleRes: Int = R.string.filter_title_prices,
        val selectedCount: Int,
        val isExpanded: Boolean,
    ) : SearchConditionFeedItem()

    data class SearchWorkConditionPriceRangeItem(
        val priceRange: PriceRange,
    ) : SearchConditionFeedItem()

    // 素材技法
    object SearchWorkConditionMaterialsTechniqueDivider : SearchConditionFeedItem()

    data class SearchWorkConditionMaterialsTechniqueHeader(
        @StringRes val titleRes: Int = R.string.filter_title_materials_techniques,
        val selectedCount: Int,
        val isExpanded: Boolean,
    ) :
        SearchConditionFeedItem()

    data class SearchWorkConditionMaterialsTechniqueItem(val materialsTechnique: ToyMaterial, val isSelected: Boolean) :
        SearchConditionFeedItem(materialsTechnique.id.value)

    data class SearchWorkConditionMaterialsTechniqueShowAll(val isExpanded: Boolean) : SearchConditionFeedItem()

    // 色カテゴリ
    object SearchWorkConditionColorCategoryDivider : SearchConditionFeedItem()

    data class SearchWorkConditionColorCategoryHeader(
        @StringRes val titleRes: Int = R.string.filter_title_color_categories,
        val selectedCount: Int,
        val isExpanded: Boolean,
    ) : SearchConditionFeedItem()

    data class SearchWorkConditionColorCategoryItem(
        val colorCategory: ToyColor,
        val isSelected: Boolean,
    ) : SearchConditionFeedItem(colorCategory.id.value)

    data class SearchWorkConditionColorCategoryShowAll(val isExpanded: Boolean) : SearchConditionFeedItem()

    // 製作年
    object SearchWorkConditionProductionYearsTypeDivider : SearchConditionFeedItem()

    data class SearchWorkConditionProductionYearsTypeHeader(
        @StringRes val titleRes: Int = R.string.filter_title_years,
        val selectedCount: Int,
        val isExpanded: Boolean,
    ) : SearchConditionFeedItem()

    data class SearchWorkConditionProductionYearsTypeItem(
        val yearsType: WorkProductionYearsType,
        val isSelected: Boolean,
    ) : SearchConditionFeedItem(yearsType.ordinal.toLong())

    data class SearchWorkConditionProductionYearsTypeShowAll(val isExpanded: Boolean) : SearchConditionFeedItem()
}
