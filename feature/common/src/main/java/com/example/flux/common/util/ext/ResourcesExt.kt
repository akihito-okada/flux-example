package com.example.flux.common.util.ext

import android.content.res.Configuration
import android.content.res.Resources
import com.example.flux.common.R

object ResourcesExt {

    val Resources.statusBarHeight: Int
        get() {
            val resourceId = getIdentifier("status_bar_height", "dimen", "android")
            return if (resourceId > 0) {
                getDimensionPixelSize(resourceId)
            } else {
                0
            }
        }
    val Resources.screenHeight get() = Resources.getSystem().displayMetrics.heightPixels
    val Resources.screenWidth: Int
        get() {
            val screenWidth = Resources.getSystem().displayMetrics.widthPixels
            val tabletMaxWidth = getDimensionPixelSize(R.dimen.tablet_max_width)
            return if (screenWidth > tabletMaxWidth) tabletMaxWidth else screenWidth
        }

    fun Resources.pxToDp(px: Int): Int {
        return (px / displayMetrics.density).toInt()
    }

    val Resources.isOnNightMode: Boolean
        get() =
            when (
                configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
            ) {
                Configuration.UI_MODE_NIGHT_YES -> true
                Configuration.UI_MODE_NIGHT_NO -> false
                Configuration.UI_MODE_NIGHT_UNDEFINED -> false
                else -> false
            }
}
