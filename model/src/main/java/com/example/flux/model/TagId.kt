package com.example.flux.model

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle

data class TagId(var value: Int = INVALID_ID) {

    fun toArguments(arguments: Bundle) {
        arguments.putInt(ARG_TAG_ID, value)
    }

    companion object {

        private const val INVALID_ID = 0

        @VisibleForTesting
        const val ARG_TAG_ID = "tag_id"

        fun fromSavedStateHandle(
            savedStateHandle: SavedStateHandle,
        ): TagId {
            return TagId(savedStateHandle.get<Int>(ARG_TAG_ID) ?: INVALID_ID)
        }

        fun fromArguments(
            arguments: Bundle?,
        ): TagId {
            return TagId(arguments?.getInt(ARG_TAG_ID) ?: INVALID_ID)
        }
    }
}
