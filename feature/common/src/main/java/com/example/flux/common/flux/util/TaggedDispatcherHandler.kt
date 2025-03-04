package com.example.flux.common.flux.util

import com.example.flux.common.flux.Action
import com.example.flux.common.flux.Dispatcher
import com.example.flux.model.TagId
import timber.log.Timber

interface TaggedDispatcherHandler {
    val dispatcher: Dispatcher
    var tagId: TagId

    fun launchAndDispatch(action: Action) {
        action.tagId = tagId
        dispatcher.launchAndDispatch(action)
    }

    fun launchAndDispatch(tagId: TagId, action: Action) {
        action.tagId = tagId
        dispatcher.launchAndDispatch(action)
    }

    suspend fun dispatch(action: Action) {
        action.tagId = tagId
        Timber.d("dispatch tag: $tagId, action: $action")
        dispatcher.dispatch(action)
    }

    suspend fun dispatch(tagId: TagId, action: Action) {
        action.tagId = tagId
        dispatcher.dispatch(action)
    }
}
