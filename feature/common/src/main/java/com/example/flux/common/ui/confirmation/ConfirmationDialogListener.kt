package com.example.flux.common.ui.confirmation

import com.example.flux.common.model.ConfirmationDialogType

interface ConfirmationDialogListener {
    fun proceedForConfirmationDialog(confirmationDialogType: ConfirmationDialogType)
    fun cancelForConfirmationDialog()
}
