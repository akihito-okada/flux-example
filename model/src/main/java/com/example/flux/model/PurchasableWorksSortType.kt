package com.example.flux.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PurchasableWorksSortType : Parcelable {
    DateAsc, PriceDesc, PriceAsc;

    val isDateAsc get() = this == DateAsc

    val value: String
        get() = when (this) {
            DateAsc -> "DATE_ASC"
            PriceDesc -> "PRICE_DESC"
            PriceAsc -> "PRICE_ASC"
        }

    val labelRes: Int
        get() = when (this) {
            DateAsc -> R.string.store_sort_type_date
            PriceDesc -> R.string.store_sort_type_price_desc
            PriceAsc -> R.string.store_sort_type_price_asc
        }

    companion object {
        val default = DateAsc

        fun fromValue(value: String): PurchasableWorksSortType {
            for (type in entries) {
                if (type.value == value) {
                    return type
                }
            }
            return DateAsc
        }

        fun fromOrdinal(ordinal: Int): PurchasableWorksSortType {
            for (type in entries) {
                if (type.ordinal == ordinal) {
                    return type
                }
            }
            return DateAsc
        }
    }
}
