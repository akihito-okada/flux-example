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

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.FontFamily

val NotoSansCjkJpMedium = FontFamily.Default

val NotoSansCjkJpRegular = FontFamily.Default

private val typography = ToysTypography()

val ToysThemeTypography = Typography(
    defaultFontFamily = NotoSansCjkJpMedium,
    h1 = typography.HeadlineMedium32L40,
    h2 = typography.HeadlineMedium28L36,
    h3 = typography.HeadlineMedium26L34,
    h4 = typography.HeadlineMedium24L32,
    h5 = typography.HeadlineMedium20L28,
    h6 = typography.HeadlineMedium18L26,
    subtitle1 = typography.SubtitleMedium18L26,
    subtitle2 = typography.SubtitleMedium16L24,
    body1 = typography.BodyRegular16L26,
    body2 = typography.BodyRegular14L22,
    button = typography.SubtitleMedium16L24,
    caption = typography.CaptionRegular12L16,
    overline = typography.InputRegular16L24,
)
