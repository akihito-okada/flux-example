package com.example.flux.remote.request.toys

import com.google.gson.annotations.SerializedName

// 保存したおもちゃを削除するリクエスト
data class RemoveSavedToyRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("toy_id") val toyId: Long
)
