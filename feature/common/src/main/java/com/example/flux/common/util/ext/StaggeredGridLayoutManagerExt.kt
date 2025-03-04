package com.example.flux.common.util.ext

import androidx.recyclerview.widget.StaggeredGridLayoutManager

object StaggeredGridLayoutManagerExt {

    fun StaggeredGridLayoutManager?.findFirstVisiblePosition(): Int {
        return this?.findFirstVisibleItemPositions(null)?.let {
            var maxSize = 0
            for (i in it.indices) {
                when {
                    i == 0 -> maxSize = it[i]
                    it[i] > maxSize -> maxSize = it[i]
                }
            }
            return maxSize
        } ?: 0
    }

    fun StaggeredGridLayoutManager?.findLastVisiblePosition(): Int {
        return this?.findLastVisibleItemPositions(null)?.let {
            var minSize = it[0]
            for (i in it.indices) {
                if (it[i] < minSize) {
                    minSize = it[i]
                }
            }
            minSize
        } ?: 0
    }
}
