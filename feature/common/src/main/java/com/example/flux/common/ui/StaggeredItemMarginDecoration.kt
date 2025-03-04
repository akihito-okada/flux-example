package com.example.flux.common.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.flux.common.R

class StaggeredItemMarginDecoration(
    private val targetResources: List<Int> = emptyList(),
    private val shouldBeTargetAll: Boolean = false,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val resources = view.resources

        if (!isTarget(view, parent)) return

        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val layoutManager = parent.layoutManager as? StaggeredGridLayoutManager
        val spanCount = layoutManager?.spanCount ?: 2

        val marginVertical = resources.getDimensionPixelSize(R.dimen.spacing_small)

        // 上下のマージンを設定
        val isFirstRow = position < spanCount
        outRect.top = if (isFirstRow) marginVertical else marginVertical / 2
        outRect.bottom = marginVertical
    }

    private fun isTarget(child: View, parent: RecyclerView): Boolean {
        if (shouldBeTargetAll) return true
        val type = parent.getChildViewHolder(child)?.itemViewType
        return type in targetResources
    }
}
