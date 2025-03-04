package com.example.flux.common.util.ext

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

object SwipeRefreshLayoutExt {
    fun SwipeRefreshLayout.hide() {
        post {
            isRefreshing = false
        }
    }
}
