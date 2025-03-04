package com.example.flux.common.model

enum class FabState {
    Expanded, Collapsed;

    val isExpanded get() = this == Expanded
    val isCollapsed get() = this == Collapsed

    companion object {
        private const val THRESHOLD_CHAT_FAB_SCROLLING_UP = 30
        private const val THRESHOLD_CHAT_FAB_SCROLLING_DOWN = 200

        fun fromDy(dy: Int): FabState? {
            return when {
                // Scrolling up
                dy > THRESHOLD_CHAT_FAB_SCROLLING_UP -> Collapsed
                // Scrolling down
                -THRESHOLD_CHAT_FAB_SCROLLING_DOWN > dy -> Expanded
                else -> null
            }
        }
    }
}
