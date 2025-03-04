package com.example.flux.remote.response

import com.example.flux.model.User
import com.example.flux.model.UserId
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
) {

    fun convert(): User {
        return User(
            id = UserId(id),
            email = email,
        )
    }
}
