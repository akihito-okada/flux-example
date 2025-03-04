package com.example.flux.common.util.navigator

import android.net.Uri
import com.example.flux.common.model.FragmentOptions
import com.example.flux.model.StoreSearchConditions
import com.example.flux.model.Toy

const val RET_RESULT_TYPE = "RET_RESULT_TYPE"

interface Navigator {

    // Fragment Tab Stack Navigation
    fun switchTab(position: Int)
    fun popFragment()
    fun popFragmentWithModal()
    fun popFragment(popDepth: Int)
    fun popSwitchTab()

    // Activity Navigation
    fun navigateToMain()
    fun navigateToMain(uri: Uri)
    fun navigateToMainWithClearTop()
    fun navigateToActionView(uri: Uri)
    fun navigateToCustomTab(url: String)
    fun navigateToBrowser(url: String)

    // Fragment Navigation
    fun navigateToToyDetail(options: FragmentOptions, toy: Toy)
    fun navigateToPurchasableWorks(options: FragmentOptions, conditions: StoreSearchConditions)
    fun navigateLink(options: FragmentOptions, uri: Uri)

    // Utils
    fun copyToClipboard(text: String)
}
