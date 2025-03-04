package com.example.flux.common.util.recyclerview

interface OnInfiniteScrollListener {
    fun clear()
    fun pause()
    fun toggle(isAllItemLoaded: Boolean)
}
