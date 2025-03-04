package com.example.flux.common.compose.theme

import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * <a href="https://material.io/design/typography/the-type-system.html#type-scale" class="external" target="_blank">Material Design type scale</a>.
 *
 * The Material Design type scale includes a range of contrasting styles that support the needs of
 * your product and its content.
 *
 * The type scale is a combination of thirteen styles that are supported by the type system. It
 * contains reusable categories of text, each with an intended application and meaning.
 *
 * ![Typography image](https://developer.android.com/images/reference/androidx/compose/material/typography.png)
 *
 */
@Immutable
class ToysTypography internal constructor(
    val HeadlineMedium32L40: TextStyle,
    val HeadlineMedium28L36: TextStyle,
    val HeadlineMedium26L34: TextStyle,
    val HeadlineMedium24L32: TextStyle,
    val HeadlineMedium20L28: TextStyle,
    val HeadlineMedium18L26: TextStyle,
    val HeadlineBold16L24: TextStyle,
    val SubtitleMedium18L26: TextStyle,
    val SubtitleMedium16L24: TextStyle,
    val SubtitleMedium14L22: TextStyle,
    val SubtitleMedium14L16: TextStyle,
    val SubtitleBold13L20: TextStyle,
    val BodyRegular16L26: TextStyle,
    val BodyRegular14L22: TextStyle,
    val BodyRegular13L20: TextStyle,
    val BodyMedium13L20: TextStyle,
    val CaptionRegular12L16: TextStyle,
    val CaptionMedium12L16: TextStyle,
    val CaptionRegular11L14: TextStyle,
    val CaptionRegular10L13: TextStyle,
    val InputRegular16L24: TextStyle,
    val InputRegular14L22: TextStyle,
    val InputRegular13L17: TextStyle,
    val InputRegular12L16: TextStyle,
) {
    /**
     * Constructor to create a [Typography]. For information on the types of style defined in
     * this constructor, see the property documentation for [Typography].
     *
     * @param defaultFontFamily the default [FontFamily] to be used for [TextStyle]s provided in
     * this constructor. This default will be used if the [FontFamily] on the [TextStyle] is `null`.
     */
    constructor(
        defaultFontFamily: FontFamily = FontFamily.Default,
        HeadlineMedium32L40: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.sp,
        ),
        HeadlineMedium28L36: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = 36.sp,
        ),
        HeadlineMedium26L34: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            lineHeight = 34.sp,
        ),
        HeadlineMedium24L32: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
        ),
        HeadlineMedium20L28: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 28.sp,
        ),
        HeadlineMedium18L26: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 26.sp,
        ),
        HeadlineBold16L24: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        SubtitleMedium18L26: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 26.sp,
        ),
        SubtitleMedium16L24: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        SubtitleMedium14L22: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 22.sp,
        ),
        SubtitleMedium14L16: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 16.sp,
        ),
        SubtitleBold13L20: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            lineHeight = 20.sp,
        ),
        BodyRegular16L26: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpRegular,
            fontSize = 16.sp,
            lineHeight = 26.sp,
        ),
        BodyRegular14L22: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpRegular,
            fontSize = 14.sp,
            lineHeight = 22.sp,
        ),
        BodyRegular13L20: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpRegular,
            fontSize = 13.sp,
            lineHeight = 20.sp,
        ),
        BodyMedium13L20: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            lineHeight = 20.sp,
        ),
        CaptionRegular12L16: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpRegular,
            fontSize = 12.sp,
            lineHeight = 16.sp,
        ),
        CaptionMedium12L16: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
        ),
        CaptionRegular11L14: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpRegular,
            fontSize = 11.sp,
            lineHeight = 14.sp,
        ),
        CaptionRegular10L13: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpRegular,
            fontSize = 10.sp,
            lineHeight = 13.sp,
        ),
        InputRegular16L24: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpRegular,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        InputRegular14L22: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpRegular,
            fontSize = 14.sp,
            lineHeight = 22.sp,
        ),
        InputRegular13L17: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpRegular,
            fontSize = 13.sp,
            lineHeight = 17.sp,
        ),
        InputRegular12L16: TextStyle = TextStyle(
            fontFamily = NotoSansCjkJpRegular,
            fontSize = 12.sp,
            lineHeight = 16.sp,
        ),
    ) : this(
        HeadlineMedium32L40 = HeadlineMedium32L40.withDefaultFontFamily(defaultFontFamily),
        HeadlineMedium28L36 = HeadlineMedium28L36.withDefaultFontFamily(defaultFontFamily),
        HeadlineMedium26L34 = HeadlineMedium26L34.withDefaultFontFamily(defaultFontFamily),
        HeadlineMedium24L32 = HeadlineMedium24L32.withDefaultFontFamily(defaultFontFamily),
        HeadlineMedium20L28 = HeadlineMedium20L28.withDefaultFontFamily(defaultFontFamily),
        HeadlineMedium18L26 = HeadlineMedium18L26.withDefaultFontFamily(defaultFontFamily),
        HeadlineBold16L24 = HeadlineBold16L24.withDefaultFontFamily(defaultFontFamily),
        SubtitleMedium18L26 = SubtitleMedium18L26.withDefaultFontFamily(defaultFontFamily),
        SubtitleMedium16L24 = SubtitleMedium16L24.withDefaultFontFamily(defaultFontFamily),
        SubtitleMedium14L22 = SubtitleMedium14L22.withDefaultFontFamily(defaultFontFamily),
        SubtitleMedium14L16 = SubtitleMedium14L16.withDefaultFontFamily(defaultFontFamily),
        SubtitleBold13L20 = SubtitleBold13L20.withDefaultFontFamily(defaultFontFamily),
        BodyRegular16L26 = BodyRegular16L26.withDefaultFontFamily(defaultFontFamily),
        BodyRegular14L22 = BodyRegular14L22.withDefaultFontFamily(defaultFontFamily),
        BodyRegular13L20 = BodyRegular13L20.withDefaultFontFamily(defaultFontFamily),
        BodyMedium13L20 = BodyMedium13L20.withDefaultFontFamily(defaultFontFamily),
        CaptionRegular12L16 = CaptionRegular12L16.withDefaultFontFamily(defaultFontFamily),
        CaptionMedium12L16 = CaptionMedium12L16.withDefaultFontFamily(defaultFontFamily),
        CaptionRegular11L14 = CaptionRegular11L14.withDefaultFontFamily(defaultFontFamily),
        CaptionRegular10L13 = CaptionRegular10L13.withDefaultFontFamily(defaultFontFamily),
        InputRegular16L24 = InputRegular16L24.withDefaultFontFamily(defaultFontFamily),
        InputRegular14L22 = InputRegular14L22.withDefaultFontFamily(defaultFontFamily),
        InputRegular13L17 = InputRegular13L17.withDefaultFontFamily(defaultFontFamily),
        InputRegular12L16 = InputRegular12L16.withDefaultFontFamily(defaultFontFamily),
    )

    /**
     * Returns a copy of this Typography, optionally overriding some of the values.
     */
    fun copy(
        HeadlineMedium32L40: TextStyle = this.HeadlineMedium32L40,
        HeadlineMedium28L36: TextStyle = this.HeadlineMedium28L36,
        HeadlineMedium26L34: TextStyle = this.HeadlineMedium26L34,
        HeadlineMedium24L32: TextStyle = this.HeadlineMedium24L32,
        HeadlineMedium20L28: TextStyle = this.HeadlineMedium20L28,
        HeadlineMedium18L26: TextStyle = this.HeadlineMedium18L26,
        HeadlineBold16L24: TextStyle = this.HeadlineBold16L24,
        SubtitleMedium18L26: TextStyle = this.SubtitleMedium18L26,
        SubtitleMedium16L24: TextStyle = this.SubtitleMedium16L24,
        SubtitleMedium14L22: TextStyle = this.SubtitleMedium14L22,
        SubtitleMedium14L16: TextStyle = this.SubtitleMedium14L16,
        SubtitleBold13L20: TextStyle = this.SubtitleBold13L20,
        BodyRegular16L26: TextStyle = this.BodyRegular16L26,
        BodyRegular14L22: TextStyle = this.BodyRegular14L22,
        BodyRegular13L20: TextStyle = this.BodyRegular13L20,
        BodyMedium13L20: TextStyle = this.BodyMedium13L20,
        CaptionRegular12L16: TextStyle = this.CaptionRegular12L16,
        CaptionMedium12L16: TextStyle = this.CaptionMedium12L16,
        CaptionRegular11L14: TextStyle = this.CaptionRegular11L14,
        CaptionRegular10L13: TextStyle = this.CaptionRegular10L13,
        InputRegular16L24: TextStyle = this.InputRegular16L24,
        InputRegular14L22: TextStyle = this.InputRegular14L22,
        InputRegular13L17: TextStyle = this.InputRegular13L17,
        InputRegular12L16: TextStyle = this.InputRegular12L16,
    ): ToysTypography = ToysTypography(
        HeadlineMedium32L40 = HeadlineMedium32L40,
        HeadlineMedium28L36 = HeadlineMedium28L36,
        HeadlineMedium26L34 = HeadlineMedium26L34,
        HeadlineMedium24L32 = HeadlineMedium24L32,
        HeadlineMedium20L28 = HeadlineMedium20L28,
        HeadlineMedium18L26 = HeadlineMedium18L26,
        HeadlineBold16L24 = HeadlineBold16L24,
        SubtitleMedium18L26 = SubtitleMedium18L26,
        SubtitleMedium16L24 = SubtitleMedium16L24,
        SubtitleMedium14L22 = SubtitleMedium14L22,
        SubtitleMedium14L16 = SubtitleMedium14L16,
        SubtitleBold13L20 = SubtitleBold13L20,
        BodyRegular16L26 = BodyRegular16L26,
        BodyRegular14L22 = BodyRegular14L22,
        BodyRegular13L20 = BodyRegular13L20,
        BodyMedium13L20 = BodyMedium13L20,
        CaptionRegular12L16 = CaptionRegular12L16,
        CaptionMedium12L16 = CaptionMedium12L16,
        CaptionRegular11L14 = CaptionRegular11L14,
        CaptionRegular10L13 = CaptionRegular10L13,
        InputRegular16L24 = InputRegular16L24,
        InputRegular14L22 = InputRegular14L22,
        InputRegular13L17 = InputRegular13L17,
        InputRegular12L16 = InputRegular12L16,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ToysTypography) return false

        if (HeadlineMedium32L40 != other.HeadlineMedium32L40 != other) return false
        if (HeadlineMedium28L36 != other.HeadlineMedium28L36 != other) return false
        if (HeadlineMedium26L34 != other.HeadlineMedium26L34 != other) return false
        if (HeadlineMedium24L32 != other.HeadlineMedium24L32 != other) return false
        if (HeadlineMedium20L28 != other.HeadlineMedium20L28 != other) return false
        if (HeadlineMedium18L26 != other.HeadlineMedium18L26 != other) return false
        if (HeadlineBold16L24 != other.HeadlineBold16L24 != other) return false
        if (SubtitleMedium18L26 != other.SubtitleMedium18L26 != other) return false
        if (SubtitleMedium16L24 != other.SubtitleMedium16L24 != other) return false
        if (SubtitleMedium14L22 != other.SubtitleMedium14L22 != other) return false
        if (SubtitleMedium14L16 != other.SubtitleMedium14L16 != other) return false
        if (SubtitleBold13L20 != other.SubtitleBold13L20 != other) return false
        if (BodyRegular16L26 != other.BodyRegular16L26 != other) return false
        if (BodyRegular14L22 != other.BodyRegular14L22 != other) return false
        if (BodyRegular13L20 != other.BodyRegular13L20 != other) return false
        if (BodyMedium13L20 != other.BodyMedium13L20 != other) return false
        if (CaptionRegular12L16 != other.CaptionRegular12L16 != other) return false
        if (CaptionMedium12L16 != other.CaptionMedium12L16 != other) return false
        if (CaptionRegular11L14 != other.CaptionRegular11L14 != other) return false
        if (CaptionRegular10L13 != other.CaptionRegular10L13 != other) return false
        if (InputRegular16L24 != other.InputRegular16L24 != other) return false
        if (InputRegular14L22 != other.InputRegular14L22 != other) return false
        if (InputRegular13L17 != other.InputRegular13L17 != other) return false
        if (InputRegular12L16 != other.InputRegular12L16) return false

        return true
    }

    override fun hashCode(): Int {
        var result = HeadlineMedium32L40.hashCode()
        result = 31 * result + HeadlineMedium28L36.hashCode()
        result = 31 * result + HeadlineMedium26L34.hashCode()
        result = 31 * result + HeadlineMedium24L32.hashCode()
        result = 31 * result + HeadlineMedium20L28.hashCode()
        result = 31 * result + HeadlineMedium18L26.hashCode()
        result = 31 * result + HeadlineBold16L24.hashCode()
        result = 31 * result + SubtitleMedium18L26.hashCode()
        result = 31 * result + SubtitleMedium16L24.hashCode()
        result = 31 * result + SubtitleMedium14L22.hashCode()
        result = 31 * result + SubtitleMedium14L16.hashCode()
        result = 31 * result + SubtitleBold13L20.hashCode()
        result = 31 * result + BodyRegular16L26.hashCode()
        result = 31 * result + BodyRegular14L22.hashCode()
        result = 31 * result + BodyRegular13L20.hashCode()
        result = 31 * result + BodyMedium13L20.hashCode()
        result = 31 * result + CaptionRegular12L16.hashCode()
        result = 31 * result + CaptionMedium12L16.hashCode()
        result = 31 * result + CaptionRegular11L14.hashCode()
        result = 31 * result + CaptionRegular10L13.hashCode()
        result = 31 * result + InputRegular16L24.hashCode()
        result = 31 * result + InputRegular14L22.hashCode()
        result = 31 * result + InputRegular13L17.hashCode()
        result = 31 * result + InputRegular12L16.hashCode()
        return result
    }

    override fun toString(): String {
        return "Typography(HeadlineMedium32L40=$HeadlineMedium32L40, " +
            "HeadlineMedium28L36=$HeadlineMedium28L36, " +
            "HeadlineMedium26L34=$HeadlineMedium26L34, " +
            "HeadlineMedium24L32=$HeadlineMedium24L32, " +
            "HeadlineMedium20L28=$HeadlineMedium20L28, " +
            "HeadlineMedium18L26=$HeadlineMedium18L26, " +
            "HeadlineBold16L24=$HeadlineBold16L24, " +
            "SubtitleMedium18L26=$SubtitleMedium18L26, " +
            "SubtitleMedium16L24=$SubtitleMedium16L24, " +
            "SubtitleMedium14L22=$SubtitleMedium14L22, " +
            "SubtitleMedium14L16=$SubtitleMedium14L16, " +
            "SubtitleBold13L20=$SubtitleBold13L20, " +
            "BodyRegular16L26=$BodyRegular16L26, " +
            "BodyRegular14L22=$BodyRegular14L22, " +
            "BodyRegular13L20=$BodyRegular13L20, " +
            "BodyMedium13L20=$BodyMedium13L20, " +
            "CaptionRegular12L16=$CaptionRegular12L16, " +
            "CaptionMedium12L16=$CaptionMedium12L16, " +
            "CaptionRegular11L14=$CaptionRegular11L14, " +
            "CaptionRegular10L13=$CaptionRegular10L13, " +
            "InputRegular16L24=$InputRegular16L24, " +
            "InputRegular14L22=$InputRegular14L22, " +
            "InputRegular13L17=$InputRegular13L17, " +
            "InputRegular12L16=$InputRegular12L16)"
    }
}

/**
 * @return [this] if there is a [FontFamily] defined, otherwise copies [this] with [default] as
 * the [FontFamily].
 */
private fun TextStyle.withDefaultFontFamily(default: FontFamily): TextStyle {
    return if (fontFamily != null) this else copy(fontFamily = default)
}

/**
 * This CompositionLocal holds on to the current definition of typography for this application as
 * described by the Material spec. You can read the values in it when creating custom components
 * that want to use Material types, as well as override the values when you want to re-style a
 * part of your hierarchy. Material components related to text such as [Button] will use this
 * CompositionLocal to set values with which to style children text components.
 *
 * To access values within this CompositionLocal, use [MaterialTheme.typography].
 */
internal val LocalToysTypography = staticCompositionLocalOf { ToysTypography() }
