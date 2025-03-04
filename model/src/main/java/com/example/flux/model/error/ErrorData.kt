package com.example.flux.model.error

data class ErrorData(
    val errors: List<String>? = arrayListOf(),
    val fieldErrors: List<FieldError>? = arrayListOf(),
    var statusCode: Int? = 0,
    val shouldAppear: Boolean? = true,
    val title: String? = "",
    val message: String? = "",
)
