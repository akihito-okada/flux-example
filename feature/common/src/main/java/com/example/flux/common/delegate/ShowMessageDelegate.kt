package com.example.flux.common.delegate

import androidx.annotation.StringRes
import com.example.flux.common.R
import com.example.flux.common.flux.actions.GlobalActions
import com.example.flux.common.flux.Dispatcher
import com.example.flux.common.flux.util.DispatcherHandler
import com.example.flux.common.model.DialogMessage
import com.example.flux.common.model.SnackBarActionType
import com.example.flux.common.model.SnackbarMessage
import com.example.flux.model.util.ResourceProvider
import com.google.android.material.snackbar.Snackbar

interface ShowMessageDelegate {
    val dispatcher: Dispatcher
    fun showSnackbarMessage(@StringRes messageRes: Int, length: Int = Snackbar.LENGTH_SHORT, snackBarActionType: SnackBarActionType = SnackBarActionType.None)
    fun showSnackbarMessage(message: String, length: Int = Snackbar.LENGTH_SHORT, snackBarActionType: SnackBarActionType = SnackBarActionType.None)
    fun showNetworkErrorSnackBarMessage()
    fun showDialogMessage(titleRes: Int, messageRes: Int)
    fun showDialogMessage(title: String, message: String)
}

class ShowMessageDelegateImpl(
    override val dispatcher: Dispatcher,
    private val resourceProvider: ResourceProvider,
) : ShowMessageDelegate, DispatcherHandler {

    override fun showSnackbarMessage(@StringRes messageRes: Int, length: Int, snackBarActionType: SnackBarActionType) {
        val action = GlobalActions.SnackbarMessageRequest(
            SnackbarMessage(
                message = resourceProvider.getString(messageRes),
                duration = length,
                snackBarActionType = snackBarActionType,
            ),
        )
        launchAndDispatch(action)
    }

    override fun showSnackbarMessage(message: String, length: Int, snackBarActionType: SnackBarActionType) {
        val action = GlobalActions.SnackbarMessageRequest(SnackbarMessage(message = message, duration = length, snackBarActionType = snackBarActionType))
        launchAndDispatch(action)
    }

    override fun showNetworkErrorSnackBarMessage() {
        showSnackbarMessage(R.string.network_error)
    }

    override fun showDialogMessage(titleRes: Int, messageRes: Int) {
        val action = GlobalActions.DialogMessageRequest(DialogMessage.DynamicDialogMessage(titleResId = titleRes, messageResId = messageRes))
        launchAndDispatch(action)
    }

    override fun showDialogMessage(title: String, message: String) {
        val action = GlobalActions.DialogMessageRequest(DialogMessage.DynamicDialogMessage(titleString = title, messageString = message, isSetResId = false))
        launchAndDispatch(action)
    }
}
