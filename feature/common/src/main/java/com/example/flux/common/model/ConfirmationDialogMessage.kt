package com.example.flux.common.model

import android.content.res.Resources
import android.os.Bundle
import android.os.Parcelable
import com.example.flux.model.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConfirmationDialogMessage constructor(
    val titleResId: Int = R.string.blank,
    val messageResId: Int = R.string.blank,
    val positiveTextRestId: Int = R.string.ok,
    val negativeTextRestId: Int = R.string.cancel,
    val shouldShowNegativeButton: Boolean = false,
    val confirmationDialogTypeOrdinal: Int = ConfirmationDialogType.Unknown.ordinal,
) : Parcelable {

    val shouldShowTitle: Boolean
        get() {
            return titleResId != R.string.blank
        }

    val shouldShowMessage: Boolean
        get() {
            return messageResId != R.string.blank
        }

    fun getTitle(resources: Resources): String {
        return resources.getString(titleResId)
    }

    fun getMessage(resources: Resources): String {
        return resources.getString(messageResId)
    }

    fun getPositiveButtonText(resources: Resources): String {
        return resources.getString(positiveTextRestId)
    }

    fun getNegativeButtonText(resources: Resources): String {
        return resources.getString(negativeTextRestId)
    }

    fun toArguments(arguments: Bundle) {
        arguments.putParcelable(ARG_CONFIRMATION_DIALOG_MESSAGE, this)
    }

    companion object {

        private const val ARG_CONFIRMATION_DIALOG_MESSAGE = "confirmation_dialog_message"

        fun fromArguments(arguments: Bundle?): ConfirmationDialogMessage {
            return (arguments?.getParcelable(ARG_CONFIRMATION_DIALOG_MESSAGE) as? ConfirmationDialogMessage)
                ?: ConfirmationDialogMessage(messageResId = R.string.blank)
        }
    }
}
