package com.example.flux.remote.util

import android.annotation.SuppressLint
import java.util.*

const val HTTP_HEADER_VALUE_UTF8 = "utf-8"
const val HTTP_HEADER_VALUE_APPLICATION_JSON = "application/json"

enum class HttpHeader(val attributeName: String) {
    Authorization("Authorization"),
    Authentication("Authentication"),
    AcceptLanguage("Accept-Language"),
    AcceptEncoding("Accept-Encoding"),
    ContentEncoding("Content-Encoding"),
    ContentType("Content-Type"),
    UserAgent("User-Agent"),
    XDeviceIdentifier("X-Device-Identifier"),
    XAndroidManufacture("X-Android-Manufacture"),
    XAndroidModel("X-Android-Model"),
    XAndroidVersion("X-Android-Version"),
    XAppVersion("X-App-Version"),
    XVersionCode("X-Version-Code"),
    ;

    companion object {

        @SuppressLint("ConstantLocale")
        val currentLang = "${Locale.getDefault().language}-${Locale.getDefault().country}"
    }
}
