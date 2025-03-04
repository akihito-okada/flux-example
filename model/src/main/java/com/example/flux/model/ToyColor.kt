package com.example.flux.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ToyColor(
    val id: ToyColorId,
    val name: String,
): Parcelable

