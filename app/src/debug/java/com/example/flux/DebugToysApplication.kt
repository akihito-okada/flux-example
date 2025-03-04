package com.example.flux

import android.annotation.SuppressLint
import leakcanary.LeakCanary

class DebugToysApplication : ToysApplication() {

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate() {
        super.onCreate()
        setupLeakCanaryIfNeeded()
    }

    private fun setupLeakCanaryIfNeeded() {
        if (environmentConfig.isDebug.not()) {
            return
        }
        LeakCanary.config = LeakCanary.config.copy(dumpHeap = false)
    }
}
