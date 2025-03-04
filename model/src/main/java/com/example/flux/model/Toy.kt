package com.example.flux.model

import com.example.flux.model.util.TimeDelegate

data class Toy(
    val id: ToyId = ToyId(),
    val name: String = "",
    val makerName: String = "",
    val image: String = "",
    val description: String = "",
    var hasSaved: Boolean = false,
    val category: ToyCategory = ToyCategory(),
    val materials: List<ToyMaterial> = listOf(),
    val dimensions: String = "",
    val price: Int = 0,
    val colors: List<ToyColor> = listOf(),
    val workCreatedAt: Int = 0,
    val createdAt: String = "",
    val publishedAt: String = "",
) : BaseItem, TimeDelegate {

    val createdAtByDate = parseForApi(createdAt)
    val publishedAtByDate = parseForApi(publishedAt)
}

