package com.example.flux.common.model

import com.google.android.material.snackbar.Snackbar

data class SnackbarMessage constructor(
    val message: String = "",
    val duration: Int = Snackbar.LENGTH_SHORT,
    val snackBarActionType: SnackBarActionType = SnackBarActionType.None,
) {
    val isValid = message != ""
}
