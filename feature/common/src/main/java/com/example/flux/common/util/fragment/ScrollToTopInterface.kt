package com.example.flux.common.util.fragment

interface ScrollToTopInterface {
    fun onScrollToTop(onTopAlready: (() -> Unit)? = null)
}
