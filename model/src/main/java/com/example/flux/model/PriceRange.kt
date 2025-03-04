package com.example.flux.model

import android.content.res.Resources
import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Parcelize
@Stable
data class PriceRange(
    val minPrice: Price = Price(DEFAULT_PRICE_MIN),
    val maxPrice: Price = Price(DEFAULT_PRICE_MAX),
    var revision: Int = 0,
) : Parcelable {

    val isDefault get() = minPrice.isRangeMin && maxPrice.isRangeMax

    fun getLabel(resources: Resources): String {
        val minPrice = resources.getString(R.string.price_format, minPrice.value)
        val maxPrice = if (maxPrice.isRangeMax) resources.getString(R.string.store_index_refine_search_nolimit) else resources.getString(R.string.price_format, maxPrice.value)
        return resources.getString(R.string.price_range_label, minPrice, maxPrice)
    }

    companion object {
        const val DEFAULT_PRICE_MAX = 1000000
        const val DEFAULT_PRICE_MIN = 0
    }
}
