package com.example.flux.common.ui.messagepopup

import com.example.flux.common.model.DialogMessage

interface MessagePopUpListener {
    fun onResultMessageDialog(
        dialogMessage: DialogMessage,
        result: MessageDialogResult,
    )
}

sealed class MessageDialogResult {
    object OnClickPositive : MessageDialogResult()
    object OnClickNegative : MessageDialogResult()
    object OnDismiss : MessageDialogResult()
}
