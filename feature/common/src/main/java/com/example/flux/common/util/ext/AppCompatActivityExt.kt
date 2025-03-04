package com.example.flux.common.util.ext

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.example.flux.common.R
import com.example.flux.common.model.DialogMessage
import com.example.flux.common.model.SystemBarType
import com.example.flux.common.ui.MessageDialogFragment
import com.example.flux.common.util.ext.ContextExt.getColorCompat
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

object AppCompatActivityExt {

    fun AppCompatActivity.isLaunchedFromHistory(): Boolean {
        val flags = intent.flags
        return flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY != 0
    }

    fun AppCompatActivity.showMessageDialog(dialogMessage: DialogMessage) {
        if (dialogMessage.isInvalid()) {
            return
        }
        MessageDialogFragment.show(supportFragmentManager, dialogMessage)
    }

    private fun AppCompatActivity.setStatusBarColor(colorRes: Int) {
        window.statusBarColor = getColorCompat(colorRes)
    }

    private fun AppCompatActivity.setNavigationBarColor(colorRes: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        window.navigationBarColor = getColorCompat(colorRes)
    }

    private fun AppCompatActivity.setSystemBarColorPrimary() {
        setLightBackgroundSystemBar()
        setStatusBarColor(R.attr.colorPrimary)
        setNavigationBarColor(R.attr.colorPrimary)
    }

    private fun AppCompatActivity.setSystemBarColorPrimaryDark() {
        if (isOnNightMode) {
            setSystemBarColorPrimary()
            return
        }
        setDarkBackgroundSystemBar()
        setStatusBarColor(R.attr.colorPrimaryDark)
        setNavigationBarColor(R.attr.colorPrimaryDark)
    }

    private fun AppCompatActivity.setSystemBarColorNowPlayingDark() {
        if (isOnNightMode) {
            setSystemBarColorPrimary()
            return
        }
        // StatusBarの色は維持
        setDarkBackgroundNavigationBar()
        setNavigationBarColor(R.attr.colorArtistDetailBackground)
    }

    @Suppress("unused")
    private fun AppCompatActivity.setSystemBarColorArtistDetailDark() {
        if (isOnNightMode) {
            setSystemBarColorPrimary()
            return
        }
        setDarkBackgroundStatusBar()
        setLightBackgroundNavigationBar()
        setStatusBarColor(R.attr.colorArtistDetailBackground)
        setNavigationBarColor(R.attr.colorPrimary)
    }

    private fun AppCompatActivity.setSystemBarColorPrimaryTransparent() {
        setLightBackgroundSystemBar()
        setStatusBarColor(R.color.transparent)
        setNavigationBarColor(R.attr.colorPrimary)
    }

    private fun AppCompatActivity.setSystemBarColorPrimaryDarkTransparent() {
        if (isOnNightMode) {
            setSystemBarColorPrimaryTransparent()
            return
        }
        setDarkBackgroundSystemBar()
        setStatusBarColor(R.color.transparent)
        setNavigationBarColor(R.attr.colorPrimaryDark)
    }

    fun AppCompatActivity.updateSystemBar(systemBarType: SystemBarType) {
        when (systemBarType) {
            SystemBarType.Primary -> setSystemBarColorPrimary()
            SystemBarType.PrimaryDark -> setSystemBarColorPrimaryDark()
            SystemBarType.PrimaryTransparent -> setSystemBarColorPrimaryTransparent()
            SystemBarType.PrimaryDarkTransparent -> setSystemBarColorPrimaryDarkTransparent()
            SystemBarType.NowPlaying -> setSystemBarColorNowPlayingDark()
            else -> setSystemBarColorPrimary()
        }
    }

    private fun AppCompatActivity.setLightBackgroundSystemBar() {
        if (isOnNightMode) {
            window.setDarkBackgroundSystemBar()
            return
        }
        window.setLightBackgroundSystemBar()
    }

    private fun AppCompatActivity.setLightBackgroundNavigationBar() {
        if (isOnNightMode) {
            return
        }
        window.setLightBackgroundNavigationBar()
    }

    @Suppress("unused")
    private fun AppCompatActivity.setLightBackgroundStatusBar() {
        if (isOnNightMode) {
            return
        }
        window.setLightBackgroundStatusBar()
    }

    private fun AppCompatActivity.setDarkBackgroundSystemBar() {
        window.setDarkBackgroundSystemBar()
    }

    private fun AppCompatActivity.setDarkBackgroundStatusBar() {
        window.setDarkBackgroundStatusBar()
    }

    private fun AppCompatActivity.setDarkBackgroundNavigationBar() {
        window.setDarkBackgroundNavigationBar()
    }

    // 明るい背景にしたいとき
    private fun Window.setLightBackgroundSystemBar() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                insetsController?.run {
                    WindowInsetsControllerCompat.toWindowInsetsControllerCompat(this).isAppearanceLightNavigationBars = true
                    WindowInsetsControllerCompat.toWindowInsetsControllerCompat(this).isAppearanceLightStatusBars = true
                }
            }

            else -> {
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    // 明るい背景のNavigationBarにしたいとき
    private fun Window.setLightBackgroundNavigationBar() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                insetsController?.run {
                    WindowInsetsControllerCompat.toWindowInsetsControllerCompat(this).isAppearanceLightNavigationBars = true
                }
            }

            else -> {
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
        }
    }

    // 明るい背景のStatusBarにしたいとき
    private fun Window.setLightBackgroundStatusBar() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                insetsController?.run {
                    WindowInsetsControllerCompat.toWindowInsetsControllerCompat(this).isAppearanceLightStatusBars = true
                }
            }

            else -> {
                decorView.systemUiVisibility = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    private fun Window.setDarkBackgroundSystemBar() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                insetsController?.run {
                    WindowInsetsControllerCompat.toWindowInsetsControllerCompat(this).isAppearanceLightNavigationBars = false
                    WindowInsetsControllerCompat.toWindowInsetsControllerCompat(this).isAppearanceLightStatusBars = false
                }
            }

            else -> {
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility = decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv() and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    }

    private fun Window.setDarkBackgroundStatusBar() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                insetsController?.run {
                    WindowInsetsControllerCompat.toWindowInsetsControllerCompat(this).isAppearanceLightStatusBars = false
                }
            }

            else -> {
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility = decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    }

    private fun Window.setDarkBackgroundNavigationBar() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                insetsController?.run {
                    WindowInsetsControllerCompat.toWindowInsetsControllerCompat(this).isAppearanceLightNavigationBars = false
                }
            }

            else -> {
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility = decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
        }
    }

    private val AppCompatActivity.isOnNightMode: Boolean
        get() =
            when (
                resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
            ) {
                Configuration.UI_MODE_NIGHT_YES -> true
                Configuration.UI_MODE_NIGHT_NO -> false
                Configuration.UI_MODE_NIGHT_UNDEFINED -> false
                else -> false
            }

    fun AppCompatActivity.mainThreadPostDelayed(invoke: (() -> Unit)?, delayTime: Long) {
        invoke ?: return
        lifecycleScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                Timber.d(throwable)
            },
        ) {
            delay(delayTime)
            invoke()
        }
    }
}
