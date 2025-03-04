package com.example.flux.common.model

import com.example.flux.model.R

enum class BottomNavigation {
    Store,
    Unknown,
    ;

    val isUnknown get() = this == Unknown

    val defaultIcon: Int
        get() = when (this) {
            Store -> R.drawable.ic_bottom_navigation_store
            Unknown -> 0
        }

    val activeIcon: Int
        get() = when (this) {
            Store -> R.drawable.ic_bottom_navigation_store_active
            Unknown -> 0
        }

    val title: Int
        get() = when (this) {
            Store -> R.string.nav_store
            Unknown -> R.string.blank
        }

    companion object {
        val defaultTab = Store
        val size get() = BottomNavigation.values().size - 1

        fun fromOrdinal(ordinal: Int): BottomNavigation {
            entries.map {
                if (it.ordinal == ordinal) {
                    return it
                }
            }
            return Store
        }
    }
}
