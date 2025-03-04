package com.example.flux.common.ui.messagepopup.item

import androidx.compose.runtime.Composable
import com.example.flux.common.ui.messagepopup.MessageDialogResult
import com.example.flux.common.ui.messagepopup.MessagePopUpListener
import com.example.flux.common.model.DialogMessage

@Composable
fun MessagePopUpScreen(
    dialogMessage: DialogMessage,
    listener: MessagePopUpListener?,
    onDismiss: () -> Unit = {},
) {
    MessagePopUpContent(
        dialogMessage,
        onClickPositive = {
            listener?.onResultMessageDialog(dialogMessage, MessageDialogResult.OnClickPositive)
            onDismiss.invoke()
        },
        onClickNegative = {
            listener?.onResultMessageDialog(dialogMessage, MessageDialogResult.OnClickNegative)
            onDismiss.invoke()
        },
        onClickClose = {
            listener?.onResultMessageDialog(dialogMessage, MessageDialogResult.OnDismiss)
            onDismiss.invoke()
        },
    )
}
