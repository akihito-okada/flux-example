package com.example.flux.remote.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("errors") val errors: List<String> = arrayListOf(),
    @SerializedName("field_errors") val fieldErrors: List<FieldErrorResponse>? = null,
    @SerializedName("status_code") var statusCode: Int = 0,
    @SerializedName("should_appear") val shouldAppear: Boolean = true,
    @SerializedName("title") val title: String = "",
    @SerializedName("message") val message: String = "",
)
