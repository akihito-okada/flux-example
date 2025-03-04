package com.example.flux.common.model

import androidx.compose.runtime.Immutable

@Immutable
data class MainScreenContent(
    val isNetworkAvailable: Boolean = false,
    val shouldShowRecomposeHighlighter: Boolean = false,
)
