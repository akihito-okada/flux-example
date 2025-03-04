package com.example.flux.model

import android.os.Build
import com.example.flux.model.util.EnvironmentConfig

class UserAgent {
    companion object {
        /**
         * Creates user agent string
         *
         * @return FluxExample/1.0.0 (com.example.flux; build:3; Android 9; Google Pixel 3 XL)
         */
        @JvmStatic
        fun value(environmentConfig: EnvironmentConfig): String {
            val strBuilder = StringBuilder().also {
                it.append("FluxExample/${environmentConfig.versionName} (")
                it.append("${environmentConfig.applicationId}; ")
                it.append("build:${environmentConfig.versionCode}; ")
                it.append("Android ${Build.VERSION.RELEASE}; ")
                it.append("${Build.MANUFACTURER} ${Build.MODEL})")
            }
            return strBuilder.toString()
        }
    }
}
