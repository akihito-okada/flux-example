/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.flux.common.compose.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val LightThemeColors = lightColors(
    primary = White,
    primaryVariant = White,
    secondary = Black,
    secondaryVariant = Black,
    surface = White,
    background = White,
    error = Red800,
    onPrimary = Black,
    onSecondary = White,
    onSurface = Black,
    onBackground = Black,
    onError = RedFF0000,
)

@SuppressLint("ConflictingOnColor")
private val DarkThemeColors = darkColors(
    primary = Black01dp,
    primaryVariant = Black01dp,
    secondary = White,
    secondaryVariant = White,
    surface = Black03dp,
    background = Black02dp,
    error = Red800,
    onPrimary = White,
    onSecondary = Black,
    onSurface = White,
    onBackground = White,
    onError = RedFF0000,
)

@Immutable
data class ExtendedColors(
    val primary: Color,
    val secondary: Color,
    val backgroundBright: Color,
    val backgroundPale: Color,
    val backgroundLight: Color,
    val backgroundDark: Color,
    val surfaceBright: Color,
    val surfaceLight: Color,
    val surfaceMiddle: Color,
    val surfaceDeep: Color,
    val surfaceDark: Color,
    val errorPrimary: Color,
    val errorSecondary: Color,
    val onPrimaryDark: Color,
    val onPrimaryDeep: Color,
    val onSecondary: Color,
    val onBackgroundDark: Color,
    val onBackgroundDeep: Color,
    val onBackgroundMiddle: Color,
    val onBackgroundLight: Color,
    val onBackgroundBright: Color,
    val onSurfaceDark: Color,
    val onSurfaceDeep: Color,
    val onSurfaceMiddle: Color,
    val onSurfaceLight: Color,
    val onSurfaceBright: Color,
    val onErrorPrimary: Color,
    val onErrorSecondary: Color,
    val border: Color,
    val borderLight: Color,
    val borderSoft: Color,
    val borderMiddle: Color,
    val attention: Color,
    val attentionInfo: Color,
    val onAttention: Color,
    val onAttentionInfo: Color,
    val onAttentionSuccessVariant: Color,
    val stickerBlue: Color,
    val stickerRed: Color,
    val stickerSilver: Color,
    val stickerGold: Color,
    val stickerBlack: Color,
    val stickerGoldPlus: Color,
    val stickerFanta: Color,
    val onStickerBright: Color,
    val onStickerBlue: Color,
    val onStickerRed: Color,
    val onStickerSilver: Color,
    val onStickerGold: Color,
    val onStickerBlack: Color,
    val onStickerGoldPlus: Color,
    val onStickerFanta: Color,
    val surfaceDarkFixedLight: Color,
    val surfaceMiddleFixedLight: Color,
    val onSurfaceBrightFixedLight: Color,
    val surfaceDialog: Color,
    val backgroundDialog: Color,
    val borderDialog: Color,
    val ripplePrimary: Color,
    val rippleSecondary: Color,
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        primary = Color.Unspecified,
        secondary = Color.Unspecified,
        backgroundBright = Color.Unspecified,
        backgroundPale = Color.Unspecified,
        backgroundLight = Color.Unspecified,
        backgroundDark = Color.Unspecified,
        surfaceBright = Color.Unspecified,
        surfaceLight = Color.Unspecified,
        surfaceMiddle = Color.Unspecified,
        surfaceDeep = Color.Unspecified,
        surfaceDark = Color.Unspecified,
        errorPrimary = Color.Unspecified,
        errorSecondary = Color.Unspecified,
        onPrimaryDark = Color.Unspecified,
        onPrimaryDeep = Color.Unspecified,
        onSecondary = Color.Unspecified,
        onBackgroundDark = Color.Unspecified,
        onBackgroundDeep = Color.Unspecified,
        onBackgroundMiddle = Color.Unspecified,
        onBackgroundLight = Color.Unspecified,
        onBackgroundBright = Color.Unspecified,
        onSurfaceDark = Color.Unspecified,
        onSurfaceDeep = Color.Unspecified,
        onSurfaceMiddle = Color.Unspecified,
        onSurfaceLight = Color.Unspecified,
        onSurfaceBright = Color.Unspecified,
        onErrorPrimary = Color.Unspecified,
        onErrorSecondary = Color.Unspecified,
        border = Color.Unspecified,
        borderLight = Color.Unspecified,
        borderSoft = Color.Unspecified,
        borderMiddle = Color.Unspecified,
        attention = Color.Unspecified,
        attentionInfo = Color.Unspecified,
        onAttention = Color.Unspecified,
        onAttentionInfo = Color.Unspecified,
        onAttentionSuccessVariant = Color.Unspecified,
        stickerBlue = Color.Unspecified,
        stickerRed = Color.Unspecified,
        stickerSilver = Color.Unspecified,
        stickerGold = Color.Unspecified,
        stickerBlack = Color.Unspecified,
        stickerGoldPlus = Color.Unspecified,
        stickerFanta = Color.Unspecified,
        onStickerBright = Color.Unspecified,
        onStickerBlue = Color.Unspecified,
        onStickerRed = Color.Unspecified,
        onStickerSilver = Color.Unspecified,
        onStickerGold = Color.Unspecified,
        onStickerBlack = Color.Unspecified,
        onStickerGoldPlus = Color.Unspecified,
        onStickerFanta = Color.Unspecified,
        surfaceDarkFixedLight = Color.Unspecified,
        surfaceMiddleFixedLight = Color.Unspecified,
        onSurfaceBrightFixedLight = Color.Unspecified,
        surfaceDialog = Color.Unspecified,
        backgroundDialog = Color.Unspecified,
        borderDialog = Color.Unspecified,
        ripplePrimary = Color.Unspecified,
        rippleSecondary = Color.Unspecified,
    )
}

@Composable
fun AppTheme(
    isLight: Boolean = isSystemInDarkTheme().not(),
    content: @Composable () -> Unit,
) {
    val extendedColors = ExtendedColors(
        primary = if (isLight) White else Black,
        secondary = if (isLight) Black else White,
        backgroundBright = if (isLight) White else Black02dp,
        backgroundPale = if (isLight) GrayF9F9F9 else GrayF9F9F9,
        backgroundLight = if (isLight) GrayEEEEEE else GrayEEEEEE,
        backgroundDark = if (isLight) Black else White,
        surfaceBright = if (isLight) White else Black03dp,
        surfaceLight = if (isLight) GrayEEEEEE else GrayEEEEEE,
        surfaceMiddle = if (isLight) GrayCCCCCC else GrayCCCCCC,
        surfaceDeep = if (isLight) Gray888888 else Gray888888,
        surfaceDark = if (isLight) Black else White,
        errorPrimary = if (isLight) White else Black,
        errorSecondary = if (isLight) RedFF0000 else RedFF0000,
        onPrimaryDark = if (isLight) Black else White,
        onPrimaryDeep = if (isLight) Gray888888 else Gray888888,
        onSecondary = if (isLight) White else Black,
        onBackgroundDark = if (isLight) White else Black,
        onBackgroundDeep = if (isLight) Gray888888 else Gray888888,
        onBackgroundMiddle = if (isLight) GrayCCCCCC else GrayCCCCCC,
        onBackgroundLight = if (isLight) GrayEEEEEE else GrayEEEEEE,
        onBackgroundBright = if (isLight) White else Black,
        onSurfaceDark = if (isLight) Black else White,
        onSurfaceDeep = if (isLight) Gray888888 else White50percent,
        onSurfaceMiddle = if (isLight) GrayCCCCCC else White30percent,
        onSurfaceLight = if (isLight) GrayEEEEEE else White20percent,
        onSurfaceBright = if (isLight) White else Black,
        onErrorPrimary = if (isLight) RedFF0000 else RedFF0000,
        onErrorSecondary = if (isLight) White else White,
        border = if (isLight) GrayCCCCCC else GrayCCCCCC,
        borderLight = if (isLight) GrayEEEEEE else Black99000000,
        borderSoft = if (isLight) GrayE5E5E5 else Black66000000,
        borderMiddle = if (isLight) GrayCCCCCC else Black40000000,
        attention = if (isLight) Green5BACA6 else Green5BACA6,
        attentionInfo = if (isLight) RedFF4F59 else RedFF4F59,
        onAttention = if (isLight) White else White,
        onAttentionInfo = if (isLight) Green5BACA6 else Green5BACA6,
        onAttentionSuccessVariant = if (isLight) Green13857C else Green5BACA6,
        stickerBlue = if (isLight) Blue2F4EE0 else Blue2F4EE0,
        stickerRed = if (isLight) RedE22727 else RedE22727,
        stickerSilver = if (isLight) GrayB6B9BA else GrayB6B9BA,
        stickerGold = if (isLight) GoldE0B27A else GoldE0B27A,
        stickerBlack = if (isLight) Black else Black,
        stickerGoldPlus = if (isLight) GoldAF8F38 else GoldAF8F38,
        stickerFanta = if (isLight) Green75C497 else Green75C497,
        onStickerBright = if (isLight) Blue2F4EE0 else Blue2F4EE0,
        onStickerBlue = if (isLight) Blue2F4EE0 else Blue2F4EE0,
        onStickerRed = if (isLight) RedE22727 else RedE22727,
        onStickerSilver = if (isLight) GrayB6B9BA else GrayB6B9BA,
        onStickerGold = if (isLight) GoldE0B27A else GoldE0B27A,
        onStickerBlack = if (isLight) Black else Black,
        onStickerGoldPlus = if (isLight) GoldAF8F38 else GoldAF8F38,
        onStickerFanta = if (isLight) Green75C497 else Green75C497,
        surfaceDarkFixedLight = Black,
        surfaceMiddleFixedLight = GrayCCCCCC,
        onSurfaceBrightFixedLight = White,
        surfaceDialog = if (isLight) White else Black24dp,
        backgroundDialog = if (isLight) Black66000000 else Black99000000,
        borderDialog = if (isLight) GrayE5E5E5 else GrayB6B9BA,
        ripplePrimary = if (isLight) White else Black,
        rippleSecondary = if (isLight) Gray888888 else GrayCCCCCC,
    )
    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
    ) {
        MaterialTheme(
            colors = if (isLight) LightThemeColors else DarkThemeColors,
            typography = ToysThemeTypography,
            shapes = ToysShapes,
            content = content,
        )
    }
}

object AppTheme {

    val colors: ExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedColors.current

    val typography: ToysTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalToysTypography.current

    val dimens: Dimen
        @Composable
        @ReadOnlyComposable
        get() = Dimen
}
