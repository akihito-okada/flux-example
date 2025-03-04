package com.example.flux.common.util.bottomnavigation

import java.io.Serializable

private const val INVALID_INDEX = -1
const val STACK_SIZE_MIN = 1

interface TabSelectedPositionManager : Serializable {
    val isEmptyTabPosition: Boolean
    val stackSizeTabPosition: Int
    fun pushTabPosition(entry: Int)
    fun clearStackTabPosition()
    fun popPreviousTabPosition(): Int
    fun destroy()
}

class TabSelectedPositionManagerImpl : TabSelectedPositionManager {

    private val stackList: ArrayList<Int> = ArrayList()

    override val isEmptyTabPosition: Boolean
        get() = stackList.size <= STACK_SIZE_MIN

    override val stackSizeTabPosition: Int
        get() = stackList.size

    override fun pushTabPosition(entry: Int) {
        if (isAlreadyExists(entry) && entry <= stackList.size) {
            stackList.remove(Integer.valueOf(entry))
        }
        stackList.add(entry)
    }

    private fun isAlreadyExists(entry: Int): Boolean {
        return stackList.contains(entry)
    }

    override fun popPreviousTabPosition(): Int {
        if (isEmptyTabPosition) {
            return INVALID_INDEX
        }

        // Remove last element from stack
        stackList.removeAt(stackList.size - 1)

        // Return previous tab position that needs to show next
        return stackList[stackList.size - 1]
    }

    override fun clearStackTabPosition() {
        stackList.clear()
    }

    override fun destroy() {
        clearStackTabPosition()
    }
}
