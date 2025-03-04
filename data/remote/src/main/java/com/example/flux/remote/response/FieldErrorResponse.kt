package com.example.flux.remote.response

import com.google.gson.annotations.SerializedName

data class FieldErrorResponse(
    @SerializedName("key") val key: String = "",
    @SerializedName("message") val message: String = "",
)
