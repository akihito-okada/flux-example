package com.example.flux.common.util.recyclerview

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/*
*
* Reference from : https://stackoverflow.com/a/50618951/3813078
*
*/
abstract class InfiniteScrollListener : RecyclerView.OnScrollListener, OnInfiniteScrollListener {

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private var visibleThreshold = 8

    // The current offset index of data you have loaded
    private var currentPage = 0

    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0

    // True if we are still waiting for the last set of data to load.
    private var loading = true
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var direction: LoadOnScrollDirection? = null
    private var isPaused = false

    constructor(layoutManager: LinearLayoutManager, direction: LoadOnScrollDirection) {
        this.layoutManager = layoutManager
        this.direction = direction
    }

    constructor(layoutManager: LinearLayoutManager, direction: LoadOnScrollDirection, visibleThreshold: Int) {
        this.layoutManager = layoutManager
        this.direction = direction
        this.visibleThreshold = visibleThreshold
    }

    constructor(layoutManager: GridLayoutManager, direction: LoadOnScrollDirection) {
        this.layoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
        this.direction = direction
    }

    constructor(layoutManager: StaggeredGridLayoutManager, direction: LoadOnScrollDirection) {
        this.layoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
        this.direction = direction
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(view, dx, dy)
        if (isPaused) {
            return
        }
        var lastVisibleItemPosition = 0
        var firstVisibleItemPosition = 0
        val totalItemCount = layoutManager?.itemCount ?: 0
        when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = (layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                val firstVisibleItemPositions = (layoutManager as StaggeredGridLayoutManager).findFirstVisibleItemPositions(null)
                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
                firstVisibleItemPosition = getFirstVisibleItem(firstVisibleItemPositions)
            }

            is LinearLayoutManager -> {
                lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                firstVisibleItemPosition = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            }

            is GridLayoutManager -> {
                lastVisibleItemPosition = (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                firstVisibleItemPosition = (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            }
        }

        when (direction) {
            LoadOnScrollDirection.BOTTOM -> {
                // If the total item count is zero and the previous isn't, assume the
                // list is invalidated and should be reset back to initial state
                if (totalItemCount < previousTotalItemCount) {
                    this.currentPage = STARTING_PAGE_INDEX
                    this.previousTotalItemCount = totalItemCount
                    if (totalItemCount == 0) {
                        this.loading = true
                    }
                }
                // If it’s still loading, we check to see if the dataset count has
                // changed, if so we conclude it has finished loading and update the current page
                // number and total item count.
                if (loading && totalItemCount > previousTotalItemCount) {
                    loading = false
                    previousTotalItemCount = totalItemCount
                }

                // If it isn’t currently loading, we check to see if we have breached
                // the visibleThreshold and need to reload more data.
                // If we do need to reload some more data, we execute onLoadMore to fetch the data.
                // threshold should reflect how many total columns there are too
                if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
                    currentPage++
                    onLoadMore(currentPage, totalItemCount)
                    loading = true
                }
            }

            LoadOnScrollDirection.TOP -> {
                // If the total item count is zero and the previous isn't, assume the
                // list is invalidated and should be reset back to initial state
                if (totalItemCount < previousTotalItemCount) {
                    this.currentPage = STARTING_PAGE_INDEX
                    this.previousTotalItemCount = totalItemCount
                    if (totalItemCount == 0) {
                        this.loading = true
                    }
                }
                // If it’s still loading, we check to see if the dataset count has
                // changed, if so we conclude it has finished loading and update the current page
                // number and total item count.
                if (loading && totalItemCount > previousTotalItemCount) {
                    loading = false
                    previousTotalItemCount = totalItemCount
                }

                // If it isn’t currently loading, we check to see if we have breached
                // the visibleThreshold and need to reload more data.
                // If we do need to reload some more data, we execute onLoadMore to fetch the data.
                // threshold should reflect how many total columns there are too
                if (!loading && firstVisibleItemPosition < visibleThreshold) {
                    currentPage++
                    onLoadMore(currentPage, totalItemCount)
                    loading = true
                }
            }

            null -> {
                // no-op
            }
        }
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    private fun getFirstVisibleItem(firstVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in firstVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = firstVisibleItemPositions[i]
            } else if (firstVisibleItemPositions[i] > maxSize) {
                maxSize = firstVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun clear() {
        isPaused = false
        previousTotalItemCount = 0
        loading = true
        currentPage = 0
    }

    override fun pause() {
        isPaused = true
    }

    override fun toggle(isAllItemLoaded: Boolean) {
        if (isAllItemLoaded) {
            pause()
            return
        }
        clear()
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemCount: Int)

    // Topはチャット型(上で読み込み)
    // Bottomは通常(下で読み込み)
    enum class LoadOnScrollDirection {
        TOP, BOTTOM
    }

    companion object {
        // Sets the starting page index
        private const val STARTING_PAGE_INDEX = 0
    }
}
