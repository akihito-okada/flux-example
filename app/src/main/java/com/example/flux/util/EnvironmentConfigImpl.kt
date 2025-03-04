package com.example.flux.util

import android.content.Context
import com.example.flux.BuildConfig
import com.example.flux.R
import com.example.flux.model.util.EnvironmentConfig

private const val ENVIRONMENT_PRODUCTION = "production"
private const val ENVIRONMENT_DEVELOPMENT = "development"
private const val ENVIRONMENT_STAGING = "staging"

class EnvironmentConfigImpl(val context: Context) : EnvironmentConfig {
    override val apiHost: String = BuildConfig.API_HOST
    override val apiHostV1: String = "${apiHost}v1/"
    override val isDebug: Boolean = BuildConfig.DEBUG
    override val applicationId: String = BuildConfig.APPLICATION_ID
    override val buildType: String = BuildConfig.BUILD_TYPE
    override val flavor: String = BuildConfig.FLAVOR
    override val versionCode: Int = BuildConfig.VERSION_CODE
    override val versionName: String = BuildConfig.VERSION_NAME
    override val isDevelopment: Boolean = flavor == ENVIRONMENT_DEVELOPMENT
    override val isStaging: Boolean = flavor == ENVIRONMENT_STAGING
    override val isProduction: Boolean = flavor == ENVIRONMENT_PRODUCTION
    override val isProductionRelease: Boolean = isProduction && !isDebug
    override val apiHostVX: String = if (isDevelopment || isStaging) apiHostV1 else apiHostV1
    override val isCi: Boolean = BuildConfig.IS_CI

    override val isTablet: Boolean
        get() {
            return context.resources.getBoolean(R.bool.is_tablet)
        }
}
