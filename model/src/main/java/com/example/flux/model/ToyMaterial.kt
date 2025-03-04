package com.example.flux.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ToyMaterial(
    val id: ToyMaterialId,
    val name: String,
) : Parcelable

