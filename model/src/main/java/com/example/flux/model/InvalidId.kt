package com.example.flux.model

import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvalidId(override val value: Long = INVALID_ID) : BaseId(value), Parcelable {

    fun toArguments(arguments: Bundle) {
        arguments.putLong(ARG_ARTIST_ID, value)
    }

    companion object {

        private const val ARG_ARTIST_ID = "invalid_id"

        fun fromArguments(bundle: Bundle?): InvalidId {
            return InvalidId(bundle?.getLong(ARG_ARTIST_ID) ?: INVALID_ID)
        }
    }
}
