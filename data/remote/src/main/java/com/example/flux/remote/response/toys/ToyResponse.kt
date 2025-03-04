package com.example.flux.remote.response.toys

import com.example.flux.model.Toy
import com.example.flux.model.ToyCategory
import com.example.flux.model.ToyId
import com.google.gson.annotations.SerializedName

data class ToyResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("maker_name") val makerName: String,
    @SerializedName("image") val image: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("has_saved") val hasSaved: Boolean?,
    @SerializedName("category") val category: ToyCategoryResponse?,
    @SerializedName("materials") val materials: List<ToyMaterialResponse>?,
    @SerializedName("dimensions") val dimensions: String?,
    @SerializedName("price") val price: Int?,
    @SerializedName("colors") val colors: List<ToyColorResponse>?,
    @SerializedName("work_created_at") val workCreatedAt: Int?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("published_at") val publishedAt: String?,
) {

    // 変換関数
    fun convert(): Toy {
        return Toy(
            id = ToyId(id),
            name = name,
            makerName = makerName,
            image = image ?: "",
            description = description ?: "",
            hasSaved = hasSaved ?: false,
            category = category?.convert() ?: ToyCategory(),
            materials = materials?.map { it.convert() } ?: listOf(),
            dimensions = dimensions ?: "",
            price = price ?: 0,
            colors = colors?.map { it.convert() } ?: listOf(),
            workCreatedAt = workCreatedAt ?: 0,
            createdAt = createdAt ?: "",
            publishedAt = publishedAt ?: "",
        )
    }
}
