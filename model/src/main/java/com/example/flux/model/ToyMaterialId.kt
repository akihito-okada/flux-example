package com.example.flux.model

import kotlinx.parcelize.Parcelize

@Parcelize
data class ToyMaterialId(override var value: Long = INVALID_ID): BaseId(value)

