package com.example.flux.common.util.ext

import android.content.Context
import android.content.res.ColorStateList
import android.util.TypedValue
import androidx.core.content.ContextCompat

private const val INVALID_VALUE = 0

object ContextExt {

    fun Context.resolveColorResId(colorResId: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(colorResId, typedValue, true)
        return when (val attrResId = typedValue.resourceId) {
            INVALID_VALUE -> colorResId
            else -> attrResId
        }
    }

    fun Context.getColorCompat(colorRes: Int): Int {
        return ContextCompat.getColor(this, resolveColorResId(colorRes))
    }

    fun Context.getColorStateListCompat(colorRes: Int): ColorStateList? {
        return ContextCompat.getColorStateList(this, resolveColorResId(colorRes))
    }
}
