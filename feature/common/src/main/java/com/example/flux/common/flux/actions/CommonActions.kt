package com.example.flux.common.flux.actions

import com.example.flux.common.flux.Action
import com.example.flux.model.TagId

sealed class CommonActions : Action {

    override var tagId = TagId()

    // Main
    class MainSplashStateChanged(val shouldShow: Boolean? = true) : CommonActions()
    data object MainHomeInitializeNeeded : CommonActions()
}
