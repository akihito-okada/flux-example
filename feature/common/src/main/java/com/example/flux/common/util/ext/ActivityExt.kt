package com.example.flux.common.util.ext

import android.app.Activity
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.flux.common.compose.component.BottomSheetWrapper
import com.example.flux.common.compose.theme.AppTheme
import com.example.flux.common.util.bottomnavigation.FragmentNavigator

object ActivityExt {

    fun Activity.switchTab(position: Int) {
        (this as? FragmentNavigator)?.switchTab(position)
    }

    fun Activity.pushFragment(fragment: Fragment) {
        (this as? FragmentNavigator)?.pushFragment(fragment)
    }

    fun Activity.pushFragmentWithModal(fragment: Fragment) {
        (this as? FragmentNavigator)?.pushFragmentWithModal(fragment)
    }

    fun Activity.pushDialogFragment(dialogFragment: DialogFragment, tagName: String) {
        (this as? FragmentNavigator)?.pushDialogFragment(dialogFragment, tagName)
    }

    fun Activity.pushDialogFragmentToActivity(dialogFragment: DialogFragment, tagName: String) {
        (this as? FragmentNavigator)?.pushDialogFragmentToActivity(dialogFragment, tagName)
    }

    fun Activity.popFragment() {
        (this as? FragmentNavigator)?.popFragment()
    }

    fun Activity.popFragmentWithModal() {
        (this as? FragmentNavigator)?.popFragment()
    }

    fun Activity.popFragment(popDepth: Int) {
        (this as? FragmentNavigator)?.popFragment(popDepth)
    }

    fun Activity.clearStack() {
        (this as? FragmentNavigator)?.clearStack()
    }

    fun Activity.popSwitchTab() {
        (this as? FragmentNavigator)?.popSwitchTab()
    }

    private fun addContentToView(
        viewGroup: ViewGroup,
        content: @Composable (() -> Unit) -> Unit,
    ) {
        viewGroup.addView(
            ComposeView(viewGroup.context).apply {
                setContent {
                    AppTheme {
                        BottomSheetWrapper(viewGroup, this, content)
                    }
                }
            },
        )
    }

    fun Activity.showAsBottomSheet(content: @Composable (() -> Unit) -> Unit) {
        val contentView = this.findViewById(android.R.id.content) as? ViewGroup ?: return
        val rootView = contentView.rootView as? ViewGroup ?: return
        addContentToView(rootView, content)
    }
}
