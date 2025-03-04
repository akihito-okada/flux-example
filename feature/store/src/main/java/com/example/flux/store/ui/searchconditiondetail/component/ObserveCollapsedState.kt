package com.example.flux.store.ui.searchconditiondetail.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun ObserveCollapsedState(
    isExpandedTarget: Boolean,
    onChanged: suspend ((Boolean) -> Unit),
) {
    // 少なく表示するときのスクロール位置の保持
    LaunchedEffect(
        key1 = isExpandedTarget,
    ) {
        snapshotFlow {
            isExpandedTarget
        }
            .filter { it.not() }
            .distinctUntilChanged()
            .collect { isExpanded ->
                onChanged.invoke(isExpanded)
            }
    }
}
