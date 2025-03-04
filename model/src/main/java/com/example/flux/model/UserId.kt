package com.example.flux.model

import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class
UserId(val value: String = INVALID_ID) : Parcelable {

    val isValid = value != INVALID_ID

    fun toArguments(arguments: Bundle) {
        arguments.putString(ARG_USER_ID, value)
    }

    companion object {
        private const val ARG_USER_ID = "user_id"
        const val INVALID_ID = ""

        fun fromValue(value: String?): UserId {
            return UserId(value ?: INVALID_ID)
        }

        fun fromArguments(bundle: Bundle?): UserId {
            return UserId(bundle?.getString(ARG_USER_ID) ?: INVALID_ID)
        }
    }
}
