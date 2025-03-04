package com.example.flux.common.flux.store

import androidx.lifecycle.LiveData
import com.example.flux.common.flux.actions.GlobalActions
import com.example.flux.common.flux.Dispatcher
import com.example.flux.common.flux.Store
import com.example.flux.common.util.lifecycle.Event
import com.example.flux.common.model.DialogMessage
import com.example.flux.common.model.SnackbarMessage
import com.example.flux.common.model.SystemBarType
import com.example.flux.common.model.ToastMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ScreenStore @Inject constructor(
    dispatcher: Dispatcher,
) : Store() {

    // NowPlayingを含む
    val currentSystemBarType get() = systemBarChanged.value?.peekContent()?.systemBarType ?: SystemBarType.Primary

    // NowPlayingを含まない
    var currentScreenSystemBarType = SystemBarType.Unknown

    val showSnackbarMessage: LiveData<Event<SnackbarMessage>> = dispatcher
        .subscribe<GlobalActions.SnackbarMessageRequest>()
        .map { it.snackbarMessageRequest }
        .toLiveDataWithEvent(this, null)

    val showToastMessage: LiveData<Event<ToastMessage>> = dispatcher
        .subscribe<GlobalActions.ToastMessageRequest>()
        .map { it.toastMessageRequest }
        .toLiveDataWithEvent(this, null)

    val showAlertDialogMessage: LiveData<Event<DialogMessage>> = dispatcher
        .subscribe<GlobalActions.DialogMessageRequest>()
        .map { it.dialogMessageRequest }
        .toLiveDataWithEvent(this, null)

    val systemBarChanged: LiveData<Event<SystemBarData>> = dispatcher
        .subscribe<GlobalActions.SystemBarChanged>()
        .map {
            if (!it.systemBarType.isNowPlaying) {
                currentScreenSystemBarType = it.systemBarType
            }
            SystemBarData(it.systemBarType, it.shouldUpdate)
        }
        .toLiveDataWithEvent(this, null)

    data class SystemBarData(val systemBarType: SystemBarType, val shouldUpdate: Boolean)
}
