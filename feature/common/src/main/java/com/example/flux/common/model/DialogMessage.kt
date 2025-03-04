package com.example.flux.common.model

import android.content.res.Resources
import android.os.Bundle
import android.os.Parcelable
import com.example.flux.model.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class DialogMessage constructor(
    open val titleResId: Int = INVALID_STRING_RES_ID,
    open val messageResId: Int = INVALID_STRING_RES_ID,
    open val titleString: String = "",
    open val messageString: String = "",
    open val isSetResId: Boolean = true,
    val hasPositiveButton: Boolean = true,
    open val positiveTextRestId: Int = R.string.ok,
    val hasNegativeButton: Boolean = false,
    val negativeTextRestId: Int = R.string.cancel,
) : Parcelable {

    fun isInvalid(): Boolean {
        return isTitleResEmpty() && isMessageResEmpty() && titleString.isEmpty() && messageString.isEmpty()
    }

    private fun isTitleResEmpty(): Boolean {
        return titleResId == INVALID_STRING_RES_ID
    }

    private fun isMessageResEmpty(): Boolean {
        return messageResId == INVALID_STRING_RES_ID
    }

    fun getMessage(resources: Resources): String {
        return if (isSetResId && !isMessageResEmpty()) resources.getString(messageResId) else messageString
    }

    fun getTitle(resources: Resources): String {
        return if (isSetResId && !isTitleResEmpty()) resources.getString(titleResId) else titleString
    }

    @Parcelize
    class DynamicDialogMessage constructor(
        override val titleResId: Int = INVALID_STRING_RES_ID,
        override val messageResId: Int = INVALID_STRING_RES_ID,
        override val titleString: String = "",
        override val messageString: String = "",
        override val isSetResId: Boolean = true,
        override val positiveTextRestId: Int = R.string.ok,
    ) : DialogMessage(
        titleResId = titleResId,
        messageResId = messageResId,
        titleString = titleString,
        messageString = messageString,
        isSetResId = isSetResId,
        positiveTextRestId = positiveTextRestId,
    )

    object NetworkError : DialogMessage(
        titleResId = R.string.network_error,
        messageResId = R.string.blank,
        hasPositiveButton = false,
        hasNegativeButton = true,
    )

    object Unknown : DialogMessage()

    fun toArguments(arguments: Bundle) {
        arguments.putParcelable(ARG_BUNDLE_DATA, this)
    }

    companion object {

        private val INVALID_STRING_RES_ID: Int = R.string.blank
        private const val ARG_BUNDLE_DATA = "bundle_data"

        fun fromArguments(arguments: Bundle?): DialogMessage {
            return arguments?.getParcelable(ARG_BUNDLE_DATA) as? DialogMessage ?: Unknown
        }
    }
}
