package com.example.flux.common.util.ext

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.flux.common.ui.widget.SnapOnScrollListener
import com.example.flux.common.model.RecyclerViewPosition

private const val SMOOTH_SCROLL_THRESHOLD_MIN = 0
private const val SMOOTH_SCROLL_THRESHOLD_MAX = 10
private const val SMOOTH_SCROLL_DELAY = 10L

object RecyclerViewExt {

    fun RecyclerView.attachSnapHelperWithListener(
        snapHelper: SnapHelper,
        behavior: SnapOnScrollListener.Behavior = SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
        onSnapPositionChangeListener: (Int) -> Unit,
    ) {
        snapHelper.attachToRecyclerView(this)
        val snapOnScrollListener = SnapOnScrollListener(snapHelper, behavior, onSnapPositionChangeListener)
        addOnScrollListener(snapOnScrollListener)
    }

    fun RecyclerView.getScrollPositionHorizontal(): RecyclerViewPosition {
        val startView = getChildAt(0)
        val positionOffset = if (startView == null) 0 else startView.left - paddingLeft
        val currentPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                (layoutManager as? StaggeredGridLayoutManager)?.findFirstVisibleItemPositions(null)?.let {
                    getFirstVisibleItem(it)
                } ?: 0
            }

            is LinearLayoutManager -> {
                (layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: 0
            }

            is GridLayoutManager -> {
                (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            }

            else -> 0
        }
        return RecyclerViewPosition(currentPosition, positionOffset)
    }

    private fun getFirstVisibleItem(firstVisibleItemPositions: IntArray): Int {
        return if (firstVisibleItemPositions.isNotEmpty()) {
            firstVisibleItemPositions[0]
        } else {
            0
        }
    }

    fun RecyclerView.getScrollPositionVertical(): RecyclerViewPosition {
        val startView = getChildAt(0)
        val positionOffset = if (startView == null) 0 else startView.top - paddingTop
        val currentPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> (layoutManager as? StaggeredGridLayoutManager)?.findFirstVisibleItemPositions(null)?.let {
                getFirstVisibleItem(it)
            } ?: 0

            is LinearLayoutManager -> (layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: 0
            is GridLayoutManager -> (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            else -> 0
        }
        return RecyclerViewPosition(currentPosition, positionOffset)
    }

    fun RecyclerView.restoreScrollPosition(position: RecyclerViewPosition) {
        post {
            scrollToPositionWithOffset(position)
        }
    }

    fun RecyclerView.restoreScrollPosition(position: RecyclerViewPosition, callback: () -> Unit) {
        postDelayed(
            {
                scrollToPositionWithOffset(position)
                callback.invoke()
            },
            100,
        )
    }

    private fun RecyclerView.scrollToPositionWithOffset(position: RecyclerViewPosition) {
        when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                (layoutManager as? StaggeredGridLayoutManager)?.scrollToPositionWithOffset(position.currentPosition, position.positionOffset)
            }

            is LinearLayoutManager -> {
                (layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(position.currentPosition, position.positionOffset)
            }

            is GridLayoutManager -> {
                (layoutManager as? GridLayoutManager)?.scrollToPositionWithOffset(position.currentPosition, position.positionOffset)
            }
        }
    }

    fun RecyclerView.scrollToTop() {
        postDelayed(
            {
                if (isSmoothScroll()) {
                    smoothScrollToPosition(0)
                    return@postDelayed
                }
                // scrollToPositionはonScrolledのコールバックが呼ばれないので直接タブを表示させる
                scrollToPosition(0)
            },
            SMOOTH_SCROLL_DELAY,
        )
    }

    fun RecyclerView.isOnTop(): Boolean {
        return getRecyclerViewFirstVisiblePosition() == 0
    }

    private fun RecyclerView.getRecyclerViewLastVisiblePosition(): Int {
        if (layoutManager is LinearLayoutManager) {
            return (layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition() ?: 0
        }
        if (layoutManager is GridLayoutManager) {
            return (layoutManager as? GridLayoutManager)?.findLastVisibleItemPosition() ?: 0
        }
        if (layoutManager is StaggeredGridLayoutManager) {
            var lastVisibleItems: IntArray? = null
            lastVisibleItems = (layoutManager as? StaggeredGridLayoutManager)?.findLastVisibleItemPositions(lastVisibleItems)
            lastVisibleItems?.also {
                if (lastVisibleItems.isNotEmpty()) {
                    return lastVisibleItems[0]
                }
            }
        }
        return 0
    }

    private fun RecyclerView.getRecyclerViewFirstVisiblePosition(): Int {
        if (layoutManager is LinearLayoutManager) {
            return (layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: 0
        }
        if (layoutManager is GridLayoutManager) {
            return (layoutManager as? GridLayoutManager)?.findFirstVisibleItemPosition() ?: 0
        }
        if (layoutManager is StaggeredGridLayoutManager) {
            var firstVisibleItems: IntArray? = null
            firstVisibleItems = (layoutManager as? StaggeredGridLayoutManager)?.findFirstVisibleItemPositions(firstVisibleItems)
            firstVisibleItems?.also {
                if (firstVisibleItems.isNotEmpty()) {
                    return firstVisibleItems[0]
                }
            }
        }
        return 0
    }

    private fun RecyclerView.isSmoothScroll(): Boolean {
        return getRecyclerViewLastVisiblePosition() in (SMOOTH_SCROLL_THRESHOLD_MIN + 1) until SMOOTH_SCROLL_THRESHOLD_MAX
    }
}
