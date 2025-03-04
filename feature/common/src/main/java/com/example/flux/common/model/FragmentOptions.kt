package com.example.flux.common.model

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.example.flux.model.R
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

const val KEY_FRAGMENT_OPTIONS = "fragment_options"

@Parcelize
@Immutable
data class FragmentOptions constructor(
    val nameRes: Int = R.string.blank,
    val titleRes: Int = R.string.blank,
    val isShowBottomNavigation: Boolean = true,
    val isResizeSoftInputModeScreen: Boolean = false,
    val systemBarType: SystemBarType = SystemBarType.Primary,
    val shouldQueryPurchase: Boolean = true,
    val shouldControlSystemBar: Boolean = true,
    var referrerRes: Int = R.string.blank,
    val isNowPlayingVisible: Boolean = false,
    val postFabVisibilityTypeOrdinal: Int = FabVisibilityType.Hide.ordinal,
    var isDebugMode: Boolean = false,
) : Parcelable {

    val isNameInvalid get() = nameRes == 0
    val isRootFragment get() = referrerRes == R.string.title_screen_root

    @IgnoredOnParcel
    val postFabVisibilityType: FabVisibilityType = FabVisibilityType.fromOrdinal(postFabVisibilityTypeOrdinal)

    fun toArguments(arguments: Bundle) {
        arguments.putParcelable(KEY_FRAGMENT_OPTIONS, this)
    }

    companion object {

        fun fromArguments(arguments: Bundle?): FragmentOptions {
            return arguments?.getParcelable(KEY_FRAGMENT_OPTIONS) as? FragmentOptions ?: FragmentOptions(
                0,
                isShowBottomNavigation = true,
                isResizeSoftInputModeScreen = false,
            )
        }
    }
}
