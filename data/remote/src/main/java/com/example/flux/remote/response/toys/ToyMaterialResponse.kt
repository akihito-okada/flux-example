package com.example.flux.remote.response.toys

import com.example.flux.model.ToyMaterial
import com.example.flux.model.ToyMaterialId
import com.google.gson.annotations.SerializedName

data class ToyMaterialResponse(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("name") val name: String? = "",
) {

    fun convert(): ToyMaterial {
        return ToyMaterial(
            id = ToyMaterialId(id),
            name = name ?: "",
        )
    }
}
