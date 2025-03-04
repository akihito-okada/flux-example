package com.example.flux.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.example.flux.R
import com.example.flux.common.delegate.ClipboardDelegate
import com.example.flux.common.util.ext.ActivityExt.popFragment
import com.example.flux.common.util.ext.ActivityExt.popFragmentWithModal
import com.example.flux.common.util.ext.ActivityExt.popSwitchTab
import com.example.flux.common.util.ext.ActivityExt.pushFragment
import com.example.flux.common.util.ext.ActivityExt.switchTab
import com.example.flux.common.util.ext.ContextExt.getColorCompat
import com.example.flux.common.util.navigator.CustomTabUtil
import com.example.flux.common.util.navigator.Navigator
import com.example.flux.common.util.navigator.SendAction
import com.example.flux.model.util.EnvironmentConfig
import com.example.flux.common.model.FragmentOptions
import com.example.flux.model.StoreSearchConditions
import com.example.flux.model.Toy
import com.example.flux.store.ui.purchasableworks.PurchasableWorksFragment
import com.example.flux.ui.main.MainActivity
import com.example.flux.works.ui.toydetail.ToyDetailFragment
import timber.log.Timber

class NavigatorImpl(private val activity: Activity, val environmentConfig: EnvironmentConfig) : Navigator, ClipboardDelegate {

    // Fragment Tab Stack Navigation
    override fun switchTab(position: Int) {
        activity.switchTab(position)
    }

    override fun popFragment() {
        activity.popFragment()
    }

    override fun popFragment(popDepth: Int) {
        activity.popFragment(popDepth)
    }

    override fun popFragmentWithModal() {
        activity.popFragmentWithModal()
    }

    override fun popSwitchTab() {
        activity.popSwitchTab()
    }

    // Activity Navigation
    override fun navigateToMain() {
        activity.startActivity(MainActivity.createIntent(activity))
    }

    override fun navigateToMainWithClearTop() {
        activity.startActivity(MainActivity.createIntentWithClearTop(activity))
    }

    override fun navigateToMain(uri: Uri) {
        activity.startActivity(MainActivity.createIntent(activity, uri))
    }

    override fun navigateToActionView(uri: Uri) {
        try {
            activity.startActivity(SendAction.createIntent(uri))
        } catch (e: ActivityNotFoundException) {
            Timber.e(e)
        }
    }

    private fun navigateToActionView(activity: Context, uri: Uri) {
        try {
            activity.startActivity(SendAction.createIntent(uri))
        } catch (e: ActivityNotFoundException) {
            Timber.e(e)
        }
    }

    override fun navigateToCustomTab(url: String) {
        try {
            val uri = Uri.parse(url)
            val packageName = CustomTabUtil.getPackageNameToUse(activity) ?: run {
                navigateToActionView(activity, uri)
                return
            }
            val customTabsIntent = CustomTabsIntent.Builder().setShowTitle(true).setToolbarColor(activity.getColorCompat(R.attr.colorPrimary)).build()
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(activity, uri)
        } catch (e: ActivityNotFoundException) {
            Timber.e(e)
        }
    }

    override fun navigateToBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }

    override fun navigateToToyDetail(options: FragmentOptions, toy: Toy) {
        activity.pushFragment(ToyDetailFragment.instance(options, toyId = toy.id))
    }

    // Fragment Navigation
    override fun navigateToPurchasableWorks(options: FragmentOptions, conditions: StoreSearchConditions) {
        activity.pushFragment(PurchasableWorksFragment.instance(options, conditions))
    }

    override fun navigateLink(options: FragmentOptions, uri: Uri) {
        navigateToCustomTab(uri.toString())
    }

    override fun copyToClipboard(text: String) {
        copyToClipboard(activity, text)
    }
}
