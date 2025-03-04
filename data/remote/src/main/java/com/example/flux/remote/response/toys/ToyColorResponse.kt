package com.example.flux.remote.response.toys

import com.example.flux.model.ToyColor
import com.example.flux.model.ToyColorId
import com.google.gson.annotations.SerializedName

data class ToyColorResponse(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("name") val name: String? = "",
) {

    fun convert(): ToyColor {
        return ToyColor(
            id = ToyColorId(id),
            name = name ?: "",
        )
    }
}
