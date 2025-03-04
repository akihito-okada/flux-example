package com.example.flux.model

import android.content.res.Resources
import androidx.annotation.StringRes

sealed class PurchasableWorksChipsItem(@StringRes val titleRes: Int = R.string.blank) {

    fun title(resources: Resources): String {
        return when (this) {
            is ClearAllItem,
            is ProductionYearsTypeItem,
                -> resources.getString(titleRes)
            is CategoryItem -> category.name
            is ColorCategoryItem -> colorCategory.name
            is MaterialsTechniqueItem -> materialsTechnique.name
            is PriceRangeItem -> priceRange.getLabel(resources)
        }
    }

    val isClearAllItem get() = this == ClearAllItem

    object ClearAllItem : PurchasableWorksChipsItem(R.string.filter_clear_all)
    data class CategoryItem(val category: ToyCategory) : PurchasableWorksChipsItem()
    data class ColorCategoryItem(val colorCategory: ToyColor) : PurchasableWorksChipsItem()
    data class MaterialsTechniqueItem(val materialsTechnique: ToyMaterial) : PurchasableWorksChipsItem()
    data class PriceRangeItem(val priceRange: PriceRange) : PurchasableWorksChipsItem(R.string.blank)
        data class ProductionYearsTypeItem(val productionYearsType: WorkProductionYearsType) : PurchasableWorksChipsItem(productionYearsType.labelRes)
}
