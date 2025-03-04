package com.example.flux.works.ui.toydetail.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.flux.common.compose.component.LoadingView
import com.example.flux.common.compose.component.TopAppBarPrimary
import com.example.flux.common.util.navigator.Navigator
import com.example.flux.model.ToySaveRequest
import com.example.flux.works.ui.toydetail.ToyDetailActionCreator
import com.example.flux.works.ui.toydetail.ToyDetailStore

@Composable
fun ToyDetailScreen(
    actionCreator: ToyDetailActionCreator,
    store: ToyDetailStore,
    navigator: Navigator,
) {
    val uiState by store.screenContent.collectAsState()
    Scaffold(
        backgroundColor = Color.Transparent,
        topBar = {
            TopAppBarPrimary(
                title = uiState.toy.name,
                onClick = {
                    navigator.popFragment()
                },
            )
        },
    ) { paddingValues ->
        Box {
            if (uiState.toy.id.isValid) {
                ToyDetailContent(
                    uiState = uiState,
                    onSaveClick = {
                        actionCreator.keepWork(
                            ToySaveRequest(
                                toy = uiState.toy,
                                hasSaved = it,
                            ),
                        )
                    },
                )
            }
            if (uiState.loadingState.isLoading) {
                LoadingView(
                    modifier = Modifier
                        .padding(paddingValues = paddingValues),
                )
            }
        }
    }
    LaunchedEffect(uiState.toy.id) {
        if (uiState.toy.id.isValid.not()) {
            actionCreator.loadWorkDetailAbout()
        }
    }
}

