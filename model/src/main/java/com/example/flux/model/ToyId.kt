package com.example.flux.model

import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ToyId(override var value: Long = INVALID_ID) : BaseId(value), Parcelable {

    fun toArguments(arguments: Bundle) {
        arguments.putLong(ARG_TOY_ID, value)
    }

    companion object {
        private const val ARG_TOY_ID = "arg_toy_id"

        fun fromArguments(arguments: Bundle?): ToyId {
            return ToyId(arguments?.getLong(ARG_TOY_ID) ?: INVALID_ID)
        }
    }
}
