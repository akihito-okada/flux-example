package com.example.flux.model

data class User(
    val id: UserId = UserId(),
    var email: String = "",
) : BaseItem {

    val isValid = id.isValid
}
