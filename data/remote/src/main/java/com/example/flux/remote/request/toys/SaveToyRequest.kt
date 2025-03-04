package com.example.flux.remote.request.toys

import com.google.gson.annotations.SerializedName

// 保存するおもちゃのリクエスト
data class SaveToyRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("toy_id") val toyId: Long
)
