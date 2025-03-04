package com.example.flux.common.model

enum class LoadingState {
    Loading, Loaded, ProcessLoading, ProcessLoaded, Refreshing, Error;

    val isLoading get() = this == Loading || this == ProcessLoading
    val isRefreshing get() = this == Refreshing
    val hasError get() = this == Error
}
