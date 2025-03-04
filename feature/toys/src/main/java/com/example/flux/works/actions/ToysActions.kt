package com.example.flux.works.actions

import com.example.flux.common.flux.Action
import com.example.flux.common.model.LoadingState
import com.example.flux.model.TagId
import com.example.flux.model.Toy

sealed class ToysActions : Action {

    override var tagId = TagId()

    // WorkDetailAbout
    class ToyDetailUpdated(val toy: Toy) : ToysActions()
    class WorkDetailAboutLoadingStateChanged(val loadingState: LoadingState) : ToysActions()
}
