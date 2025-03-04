package com.example.flux.common.util.fragment

interface OnBackPressedListener {
    fun handleOnBackPressed()
    fun canGoBack(): Boolean
}
