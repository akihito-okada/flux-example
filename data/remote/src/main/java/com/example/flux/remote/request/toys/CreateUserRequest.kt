package com.example.flux.remote.request.toys

import com.google.gson.annotations.SerializedName

// ユーザー作成リクエスト
data class CreateUserRequest(
    @SerializedName("email") val email: String
)
