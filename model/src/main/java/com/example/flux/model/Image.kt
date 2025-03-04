package com.example.flux.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/*
 *   user icon size
 *   s 98px × 98px, 24.5dp
 *   m 166px × 166px, 41.5dp
 *   l 318px × 318px, 79.5dp
 *   xl 828px × 828px, 207 dp
*/

/*
 *   work image width size
 *   作品によって異なるのであくまで目安
 *
 *   s : 106px, 26dp
 *   m : 363px, 90dp
 *   l : 828px, 207dp
 *   xl : 1000px, 250dp
*/

@Parcelize
class Image(
    val small: String = "",
    val medium: String = "",
    val large: String = "",
    val xlarge: String = "",
) : Parcelable {

    @IgnoredOnParcel
    val isValid = small.isNotBlank() ||
        medium.isNotBlank() ||
        large.isNotBlank() ||
        xlarge.isNotBlank()

    fun optimizeIconUrl(widthDp: Int): String {
        return when (widthDp) {
            in 0..26 -> small
            in 27..41 -> medium
            in 42..79 -> large
            else -> xlarge
        }
    }

    fun optimizeWorkUrl(widthDp: Int): String {
        return when (widthDp) {
            in 0..26 -> small
            in 27..90 -> medium
            in 91..250 -> large
            else -> xlarge
        }
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Image -> small == other.small
            else -> false
        }
    }

    override fun hashCode(): Int {
        return small.hashCode()
    }
}
