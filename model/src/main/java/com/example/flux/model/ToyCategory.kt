package com.example.flux.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ToyCategory(
    val id: ToyCategoryId = ToyCategoryId(),
    val name: String = "",
) : Parcelable

