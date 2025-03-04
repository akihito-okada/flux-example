package com.example.flux.common.util.ext

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

object SnapHelperExt {

    fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
        val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        return layoutManager.getPosition(snapView)
    }
}
