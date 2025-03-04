package com.example.flux.common.delegate

import com.example.flux.common.flux.actions.GlobalActions
import com.example.flux.common.flux.Dispatcher
import com.example.flux.common.model.FabState
import com.example.flux.common.model.SystemBarType

interface GlobalActionDelegate {

    val dispatcher: Dispatcher

    fun changeSystemBar(systemBarType: SystemBarType, shouldUpdate: Boolean = true) {
        dispatcher.launchAndDispatch(GlobalActions.SystemBarChanged(systemBarType = systemBarType, shouldUpdate = shouldUpdate))
    }

    fun updateStoreFabStateIfNeeded(dy: Int) {
        val state = FabState.fromDy(dy) ?: return
        dispatcher.launchAndDispatch(GlobalActions.StoreFabStateChanged(state))
    }
}
