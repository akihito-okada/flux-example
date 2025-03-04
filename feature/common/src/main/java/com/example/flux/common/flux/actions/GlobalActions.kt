package com.example.flux.common.flux.actions

import com.example.flux.common.flux.Action
import com.example.flux.model.AppThemeType
import com.example.flux.common.model.DialogMessage
import com.example.flux.common.model.FabState
import com.example.flux.common.model.SnackbarMessage
import com.example.flux.common.model.SystemBarType
import com.example.flux.model.TagId
import com.example.flux.common.model.ToastMessage
import com.example.flux.model.ToySaveRequest

sealed class GlobalActions : Action {
    override var tagId = TagId()

    class SystemBarChanged(val systemBarType: SystemBarType, val shouldUpdate: Boolean = true) : GlobalActions()
    class DialogMessageRequest(val dialogMessageRequest: DialogMessage) : GlobalActions()
    class ToastMessageRequest(val toastMessageRequest: ToastMessage) : GlobalActions()
    class SnackbarMessageRequest(val snackbarMessageRequest: SnackbarMessage) : GlobalActions()
    class WorkKept(val toySaveRequest: ToySaveRequest) : GlobalActions()
    class MaintenanceStatusUpdated(val shouldMaintenance: Boolean) : GlobalActions()
    class ForceUpdateStatusUpdated(val shouldForceUpdate: Boolean) : GlobalActions()
    class SystemThemeChanged(val currentAppTheme: AppThemeType) : GlobalActions()
    class IsNetworkAvailableUpdated(val isAvailable: Boolean) : GlobalActions()
    class StoreFabStateChanged(val state: FabState) : GlobalActions()
    class ShouldShowRecomposeHighlighterLoaded(val shouldShow: Boolean) : GlobalActions()
}
