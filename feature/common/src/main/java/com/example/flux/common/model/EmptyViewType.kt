package com.example.flux.common.model

import com.example.flux.model.R

enum class EmptyViewType {
    PurchasableWorks,
    Unknown,
    ;

    val hasHeaderPadding
        get() = when (this) {
            PurchasableWorks,
            -> true
            else -> false
        }

    val hasHeaderPaddingSmall
        get() = false

    val titleRes
        get() = when (this) {
            PurchasableWorks -> R.string.store_search_result_refine_blank
            Unknown -> R.string.blank
        }

    val messageRes
        get() = when (this) {
            PurchasableWorks -> R.string.blank
            Unknown -> R.string.blank
        }

    val navigateActionRes
        get() = when (this) {
            PurchasableWorks -> R.string.blank
            Unknown -> R.string.blank
        }

    val hasMessage get() = messageRes != R.string.blank
    val hasNavigationButton get() = navigateActionRes != R.string.blank
}
