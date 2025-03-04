package com.example.flux.common.flux.util

import com.example.flux.common.flux.Action
import com.example.flux.common.flux.Dispatcher

interface DispatcherHandler {
    val dispatcher: Dispatcher

    fun launchAndDispatch(action: Action) {
        dispatcher.launchAndDispatch(action)
    }

    suspend fun dispatch(action: Action) {
        dispatcher.dispatch(action)
    }
}
