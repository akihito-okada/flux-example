package com.example.flux.model

enum class AppThemeType {
    Light, Dark, FollowDeviceTheme;

    val isLight get() = this == Light
    val isDark get() = this == Dark

    val itemNameRes: Int
        get() = when (this) {
            Light -> R.string.setting_root_light_app_theme
            Dark -> R.string.setting_root_dark_app_theme
            FollowDeviceTheme -> R.string.setting_root_device_theme
        }

    companion object {
        val default = FollowDeviceTheme

        fun fromOrdinal(ordinal: Int): AppThemeType {
            for (type in values()) {
                if (type.ordinal == ordinal) {
                    return type
                }
            }
            return default
        }
    }
}
