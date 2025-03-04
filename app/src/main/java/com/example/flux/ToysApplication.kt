package com.example.flux

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.memory.MemoryCache
import coil.util.DebugLogger
import com.example.flux.common.delegate.SystemDelegate
import com.example.flux.model.util.EnvironmentConfig
import com.example.flux.preferences.UserPreferences
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
open class ToysApplication : Application(), SystemDelegate, ImageLoaderFactory {

    @Inject
    lateinit var environmentConfig: EnvironmentConfig

    @Inject
    lateinit var userPreferences: UserPreferences

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate() {
        super.onCreate()
        plantTimber()
        updateAppTheme(userPreferences.loadAppTheme())
        enableCoroutinesDebugIfNeeded()
    }

    private fun enableCoroutinesDebugIfNeeded() {
        System.setProperty("kotlinx.coroutines.debug", if (environmentConfig.isDebug) "on" else "off")
    }

    private fun plantTimber() {
        if (environmentConfig.isDebug || !environmentConfig.isCi) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    // Set the max size to 25% of the app's available memory.
                    .maxSizePercent(0.25)
                    .build()
            }
            .crossfade(true) // Show a short crossfade when loading images from network or disk.
            .components {
                // GIFs
                when {
                    SDK_INT >= 28 -> add(ImageDecoderDecoder.Factory())
                    else -> add(GifDecoder.Factory())
                }
                // SVGs
                add(SvgDecoder.Factory())
            }
            .okHttpClient(okHttpClient)
            .apply {
                // Enable logging to the standard Android log if this is a debug build.
                when {
                    environmentConfig.isDebug -> logger(DebugLogger(Log.VERBOSE))
                }
            }
            .build()
    }
}
