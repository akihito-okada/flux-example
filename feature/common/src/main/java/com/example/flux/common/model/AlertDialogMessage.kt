package com.example.flux.common.model

import android.os.Bundle
import android.os.Parcelable
import com.example.flux.model.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AlertDialogMessage constructor(
    val titleResId: Int = INVALID_RES_ID,
    val messageResId: Int = INVALID_RES_ID,
    val positiveTextRestId: Int = R.string.yes,
    val negativeTextResId: Int = R.string.cancel,
) : Parcelable {

    fun isInvalid(): Boolean {
        return isTitleEmpty() && isMessageEmpty()
    }

    fun isTitleEmpty(): Boolean {
        return titleResId == INVALID_RES_ID
    }

    fun isMessageEmpty(): Boolean {
        return messageResId == INVALID_RES_ID
    }

    object Unknown : AlertDialogMessage()

    fun toArguments(arguments: Bundle) {
        arguments.putParcelable(ARG_BUNDLE_DATA, this)
    }

    companion object {
        private const val INVALID_RES_ID: Int = 0
        private const val ARG_BUNDLE_DATA = "bundle_data"

        fun fromArguments(arguments: Bundle?): AlertDialogMessage {
            return arguments?.getParcelable(ARG_BUNDLE_DATA) as? AlertDialogMessage ?: Unknown
        }
    }
}
