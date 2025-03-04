package com.example.flux.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Price(val value: Int = PRICE_INVALID) : Parcelable {
    @IgnoredOnParcel
    val isValid = value != PRICE_INVALID
    val label get() = value.toString()

    @IgnoredOnParcel
    val sliderValue = value.div(UNIT_SLIDER_PRICE).toFloat()

    @IgnoredOnParcel
    val isRangeMax = value == UNIT_SLIDER_PRICE * PRICE_RANGE_MAX

    @IgnoredOnParcel
    val isRangeMin = value == 0

    val apiRequestPriceMin get() = if (isValid && !isRangeMin) value else null
    val apiRequestPriceMax get() = if (isValid && !isRangeMax) value else null

    companion object {
        const val UNIT_SLIDER_PRICE = 10000
        const val PRICE_INVALID = -1
        private const val PRICE_RANGE_MAX = 100
    }
}
