package com.example.flux.common.delegate

import androidx.appcompat.app.AppCompatDelegate
import com.example.flux.model.AppThemeType

interface SystemDelegate {

    fun updateAppTheme(appTheme: AppThemeType) {
        AppCompatDelegate.setDefaultNightMode(
            when (appTheme) {
                AppThemeType.Light -> AppCompatDelegate.MODE_NIGHT_NO
                AppThemeType.Dark -> AppCompatDelegate.MODE_NIGHT_YES
                AppThemeType.FollowDeviceTheme -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            },
        )
    }
}
