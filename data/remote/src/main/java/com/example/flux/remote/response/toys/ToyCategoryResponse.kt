package com.example.flux.remote.response.toys

import com.example.flux.model.ToyCategory
import com.example.flux.model.ToyCategoryId
import com.google.gson.annotations.SerializedName

class ToyCategoryResponse(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("name") val name: String? = "",
) {

    fun convert(): ToyCategory {
        return ToyCategory(
            id = ToyCategoryId(id),
            name = name ?: "",
        )
    }
}
