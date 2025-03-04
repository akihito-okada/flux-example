package com.example.flux.common.model

enum class FabVisibilityType {
    Show, Hide, Unknown;

    val isUnknown get() = this == Unknown
    val shouldShow get() = this == Show

    companion object {

        fun fromOrdinal(ordinal: Int): FabVisibilityType {
            for (type in entries) {
                if (type.ordinal == ordinal) {
                    return type
                }
            }
            return Unknown
        }
    }
}
