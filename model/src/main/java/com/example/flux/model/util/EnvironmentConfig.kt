package com.example.flux.model.util

interface EnvironmentConfig {
    val apiHost: String
    val apiHostV1: String
    val apiHostVX: String
    val isDevelopment: Boolean
    val isStaging: Boolean
    val isDebug: Boolean
    val isProduction: Boolean
    val isProductionRelease: Boolean
    val applicationId: String
    val buildType: String
    val flavor: String
    val versionCode: Int
    val versionName: String
    val isCi: Boolean
    val isTablet: Boolean
}
