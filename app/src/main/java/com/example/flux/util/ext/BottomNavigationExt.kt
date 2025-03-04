package com.example.flux.util.ext

import androidx.fragment.app.Fragment
import com.example.flux.R
import com.example.flux.common.model.BottomNavigation
import com.example.flux.common.model.FragmentOptions
import com.example.flux.model.StoreSearchConditions
import com.example.flux.store.ui.purchasableworks.PurchasableWorksFragment

val BottomNavigation.fragment: Fragment
    get() = when (this) {
        BottomNavigation.Store,
        BottomNavigation.Unknown,
            -> PurchasableWorksFragment.instance(
            getFragmentOptions(),
            StoreSearchConditions(),
        )
    }

private fun getFragmentOptions(): FragmentOptions {
    return FragmentOptions(nameRes = R.string.title_screen_root)
}
