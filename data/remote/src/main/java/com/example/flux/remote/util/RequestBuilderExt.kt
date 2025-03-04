package com.example.flux.remote.util

import android.os.Build
import com.example.flux.model.util.EnvironmentConfig
import com.example.flux.model.UserAgent
import okhttp3.Request

private const val APPLICATION_JSON = "application/json"

fun Request.Builder.addHeadersForApi(environmentConfig: EnvironmentConfig): Request.Builder {
    addHeader(HttpHeader.AcceptLanguage.attributeName, HttpHeader.currentLang)
    addHeader(HttpHeader.XAndroidManufacture.attributeName, Build.MANUFACTURER)
    addHeader(HttpHeader.XAndroidModel.attributeName, Build.MODEL)
    addHeader(HttpHeader.XAndroidVersion.attributeName, Build.VERSION.RELEASE)
    addHeader(HttpHeader.XAppVersion.attributeName, environmentConfig.versionName)
    addHeader(HttpHeader.ContentType.attributeName, APPLICATION_JSON)
    addHeader(HttpHeader.UserAgent.attributeName, UserAgent.value(environmentConfig))
    addHeader(HttpHeader.XVersionCode.attributeName, environmentConfig.versionCode.toString())
    return this
}

fun Request.Builder.addHeaders(environmentConfig: EnvironmentConfig): Request.Builder {
    addHeader(HttpHeader.AcceptLanguage.attributeName, HttpHeader.currentLang)
    addHeader(HttpHeader.XAndroidManufacture.attributeName, Build.MANUFACTURER)
    addHeader(HttpHeader.XAndroidModel.attributeName, Build.MODEL)
    addHeader(HttpHeader.XAndroidVersion.attributeName, Build.VERSION.RELEASE)
    addHeader(HttpHeader.XAppVersion.attributeName, environmentConfig.versionName)
    addHeader(HttpHeader.XVersionCode.attributeName, environmentConfig.versionCode.toString())
    addHeader(HttpHeader.UserAgent.attributeName, UserAgent.value(environmentConfig))
    return this
}
