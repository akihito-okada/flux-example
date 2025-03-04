package com.example.flux.works.ui.toydetail.compose

import com.example.flux.common.model.LoadingState
import com.example.flux.model.Toy
import com.example.flux.model.ToySaveRequest

data class ToyDetailScreenContent(
    val toy: Toy = Toy(),
    val loadingState: LoadingState = LoadingState.Loaded,
    val toySaveRequest: ToySaveRequest = ToySaveRequest(),
)
