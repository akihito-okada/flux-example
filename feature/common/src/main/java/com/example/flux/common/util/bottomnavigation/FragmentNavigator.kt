package com.example.flux.common.util.bottomnavigation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

interface FragmentNavigator {
    fun switchTab(position: Int)
    fun pushFragment(fragment: Fragment)
    fun pushFragmentWithModal(fragment: Fragment)
    fun popFragment()
    fun popFragmentWithModal()
    fun popFragment(popDepth: Int)
    fun clearStack()
    fun popSwitchTab()
    fun pushDialogFragment(dialogFragment: DialogFragment, tagName: String)
    fun pushDialogFragmentToActivity(dialogFragment: DialogFragment, tagName: String)
}
