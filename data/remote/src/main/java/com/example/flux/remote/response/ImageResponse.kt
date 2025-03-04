package com.example.flux.remote.response

import com.example.flux.model.Image
import com.google.gson.annotations.SerializedName

class ImageResponse(
    @SerializedName("small")
    val small: String = "",
    @SerializedName("medium")
    val medium: String = "",
    @SerializedName("large")
    val large: String = "",
    @SerializedName("x_large")
    val xLarge: String = "",
) {

    fun convert(): Image {
        return Image(small = small, medium = medium, large = large, xlarge = xLarge)
    }
}

