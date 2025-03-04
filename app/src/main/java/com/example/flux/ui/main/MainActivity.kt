package com.example.flux.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.flux.R
import com.example.flux.common.delegate.viewBinding
import com.example.flux.common.flux.store.MainStore
import com.example.flux.common.flux.store.ScreenStore
import com.example.flux.common.model.BottomNavigation
import com.example.flux.common.model.FragmentOptions
import com.example.flux.common.model.SystemBarType
import com.example.flux.common.util.bottomnavigation.FragmentNavigator
import com.example.flux.common.util.bottomnavigation.TabStackManager
import com.example.flux.common.util.ext.AppCompatActivityExt.isLaunchedFromHistory
import com.example.flux.common.util.ext.AppCompatActivityExt.mainThreadPostDelayed
import com.example.flux.common.util.ext.AppCompatActivityExt.showMessageDialog
import com.example.flux.common.util.ext.AppCompatActivityExt.updateSystemBar
import com.example.flux.common.util.ext.FragmentExt.fragmentOptions
import com.example.flux.common.util.ext.ViewExt.showSnackbar
import com.example.flux.common.util.ext.ViewExt.toGone
import com.example.flux.common.util.ext.ViewExt.toVisible
import com.example.flux.common.util.fragment.OnBackPressedListener
import com.example.flux.common.util.fragment.OnDebugFeatureInterface
import com.example.flux.common.util.fragment.ScrollToTopInterface
import com.example.flux.common.util.lifecycle.EventObserver
import com.example.flux.common.util.navigator.Navigator
import com.example.flux.databinding.ActivityMainBinding
import com.example.flux.model.util.EnvironmentConfig
import com.example.flux.util.ext.fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(R.layout.activity_main),
    FragmentNavigator,
    OnDebugFeatureInterface {

    private val binding: ActivityMainBinding by viewBinding()

    private val isFromSetup get() = intent.getBooleanExtra(EXTRA_FROM_SETUP, false)

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var actionCreatorFactory: MainActionCreator.Factory
    private val actionCreator: MainActionCreator by lazy {
        actionCreatorFactory.create(
            lifecycle = lifecycle,
        )
    }

    @Inject
    lateinit var environmentConfig: EnvironmentConfig

    private val screenStore by viewModels<ScreenStore>()

    private val mainStore by viewModels<MainStore>()

    private var myPageBackgroundView: ImageView? = null
    private var myPageView: ImageView? = null
    private var tabStackManager: TabStackManager? = null
    private var isShowingSnackbar = false
    private var lastNavigationBarHeight = Integer.MAX_VALUE

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!environmentConfig.isTablet) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        setFullScreen()

        observeWindowInsets()

        initializeObservers(savedInstanceState)

        handleOnCreate(savedInstanceState)

        updateSystemBarType(SystemBarType.Primary)

        onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val child = (tabStackManager?.currentFragment as? OnBackPressedListener)
                    when {
                        child?.canGoBack() == true -> child.handleOnBackPressed()
                        else -> when (tabStackManager?.isRootFragment) {
                            true -> when (tabStackManager?.isEmptyTabPosition) {
                                true -> finish()

                                else -> popSwitchTab()
                            }

                            else -> tabStackManager?.popFragment()
                        }
                    }
                }
            },
        )
    }

    private fun initializeObservers(savedInstanceState: Bundle?) {
        mainStore.also { store ->
            store.showSplashNeeded.observe(
                this,
                EventObserver { isNeeded ->
                    if (isNeeded == false) {
                        hideSplash()
                        return@EventObserver
                    }
                    showSplash()
                    lifecycleScope.launch {
                        if (binding.splashLayout.isVisible) {
                            hideSplash()
                        }
                    }
                },
            )
            store.initializeNeeded.observe(
                this,
                EventObserver {
                    initialize(savedInstanceState)
                },
            )
            store.systemThemeChanged.observe(
                this,
                EventObserver { appTheme ->
                    actionCreator.updateAppTheme(appTheme)
                    navigator.navigateToMainWithClearTop()
                },
            )
        }
    }

    private fun observeWindowInsets() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU -> binding.root.setWindowInsetsAnimationCallback(
                object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
                    override fun onProgress(
                        insets: WindowInsets,
                        animations: MutableList<WindowInsetsAnimation>,
                    ): WindowInsets {
                        Timber.d("observeWindowInsets : onProgress")
                        val navigationBarHeight = insets.getInsets(WindowInsets.Type.navigationBars()).bottom
                        updateNavigationBarHeightIfNeeded(navigationBarHeight)
                        return insets
                    }
                },
            )

            else -> binding.root.setOnApplyWindowInsetsListener { _, insets ->
                Timber.d("observeWindowInsets : setOnApplyWindowInsetsListener")
                @Suppress("DEPRECATION")
                val navigationBarHeight = insets.systemWindowInsetBottom
                updateNavigationBarHeightIfNeeded(navigationBarHeight)
                insets
            }
        }
    }

    private fun updateNavigationBarHeightIfNeeded(height: Int) {
        if (height != lastNavigationBarHeight) {
            updateBottomPaddingOfViews(height)
            lastNavigationBarHeight = height
        }
    }

    private fun updateBottomPaddingOfViews(height: Int) {
        binding.splashLayout.setPadding(0, 0, 0, height)
    }

    private fun showSplash() {
        lifecycleScope.launch {
            // Orientationを指定しない場合、一瞬ガタつくので少し待ってから表示
            binding.splashLayout.toVisible()
        }
    }

    private fun hideSplash() {
        binding.splashLayout.toGone()
    }

    private fun handleOnCreate(savedInstanceState: Bundle?) {
        val isAppKilled = savedInstanceState?.getBoolean(KEY_APP_IS_KILLED) ?: false
        when {
            isFromSetup -> {
                initialize(savedInstanceState)
                hideSplash()
            }

            isAppKilled || isLaunchedFromHistory() -> {
                initialize(savedInstanceState)
                if (isAppKilled) {
                    hideNowPlayingFragment()
                }
                hideSplash()
            }

            else -> actionCreator.initialize()
        }
    }

    private fun initialize(savedInstanceState: Bundle?) {
        setupTabLayout(savedInstanceState)
        actionCreator.loadShouldShowRecomposeHighlighter()

        screenStore.also { store ->
            store.showSnackbarMessage.observe(
                this,
                EventObserver { request ->
                    if (isShowingSnackbar) {
                        return@EventObserver
                    }
                    isShowingSnackbar = true
                    binding.snackbarLayout.showSnackbar(
                        snackbarText = request.message,
                        timeLength = request.duration,
                        type = request.snackBarActionType,
                        callback = {
                            mainThreadPostDelayed(
                                {
                                    isShowingSnackbar = false
                                },
                                2000,
                            )
                        },
                        actionCallback = {
                        },
                    )
                },
            )
            store.showToastMessage.observe(
                this,
                EventObserver { message ->
                    Toast.makeText(binding.root.context, message.message, message.duration).show()
                },
            )
            store.showAlertDialogMessage.observe(
                this,
                EventObserver { request ->
                    showMessageDialog(request)
                },
            )
            store.systemBarChanged.observe(
                this,
                EventObserver { systemBarData ->
                    if (systemBarData.shouldUpdate.not()) {
                        return@EventObserver
                    }
                    updateSystemBarTypeOnEvent(systemBarData.systemBarType)
                },
            )
        }
    }

    private fun updateSystemBarTypeOnEvent(systemBarType: SystemBarType) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            // Mate 10 liteでバグることを確認。SystemBarの変更はさせない
            return
        }
        updateSystemBarType(systemBarType)
    }

    private fun updateSystemBarType(systemBarType: SystemBarType) {
        actionCreator.changeSystemBar(systemBarType = systemBarType, shouldUpdate = false)
        updateSystemBar(systemBarType = systemBarType)
    }

    private fun setFullScreen() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun onDestroy() {
        myPageView = null
        myPageBackgroundView = null
        tabStackManager?.destroy()
        tabStackManager = null
        super.onDestroy()
    }

    private fun setupTabLayout(savedInstanceState: Bundle?) {
        tabStackManager = TabStackManager
            .newBuilder(
                savedInstanceState,
                supportFragmentManager,
                R.id.nav_host_fragment_main,
            )
            .destinationChangedListener(destinationChanged)
            .rootFragmentListener(rootFragmentListener)
            .build()
        tabStackManager?.also {
            switchTab(it.selectedTabPosition)
        }
    }

    private val destinationChanged = object : TabStackManager.OnDestinationChangedListener {
        override fun onDestinationChanged(fragment: Fragment) {
            val fragmentOption: FragmentOptions = fragment.fragmentOptions
            updateSystemBarTypeIfNeeded(fragmentOption)
        }
    }

    private fun updateSystemBarTypeIfNeeded(fragmentOption: FragmentOptions) {
        if (fragmentOption.shouldControlSystemBar) {
            val systemBarType = fragmentOption.systemBarType
            if (screenStore.currentSystemBarType != systemBarType) {
                binding.root.postDelayed({ updateSystemBarType(systemBarType = systemBarType) }, 100)
            }
        }
    }

    private val rootFragmentListener = object : TabStackManager.RootFragmentListener {
        override val defaultBottomNavigationPosition: Int
            get() = BottomNavigation.defaultTab.ordinal

        override val bottomNavigationSize: Int
            get() = BottomNavigation.size

        override fun getRootFragment(position: Int): Fragment {
            return BottomNavigation.fromOrdinal(position).fragment
        }
    }

    private fun hideNowPlayingFragment() {
        supportFragmentManager.popBackStack()
    }

    override fun pushFragment(fragment: Fragment) {
        tabStackManager?.pushFragment(fragment)
    }

    override fun popFragment() {
        tabStackManager?.popFragment()
    }

    override fun popFragment(popDepth: Int) {
        tabStackManager?.popFragment(popDepth)
    }

    override fun popFragmentWithModal() {
        tabStackManager?.popFragmentWithModal()
    }

    override fun pushFragmentWithModal(fragment: Fragment) {
        tabStackManager?.pushFragmentWithModal(fragment)
    }

    override fun clearStack() {
        tabStackManager?.clearStack(
            object : TabStackManager.OnTabReselectedListener {
                override fun onScrollToTop(fragment: Fragment) {
                    (fragment as? ScrollToTopInterface)?.onScrollToTop()
                }

                override fun onClearStack(fragment: Fragment) {
                }
            },
        )
    }

    override fun switchTab(position: Int) {
        tabStackManager?.switchTab(position)
    }

    override fun popSwitchTab() {
        tabStackManager?.popTab() ?: 0
    }

    override fun pushDialogFragment(dialogFragment: DialogFragment, tagName: String) {
        val fragmentManager = tabStackManager?.currentFragment?.childFragmentManager ?: return
        if (fragmentManager.findFragmentByTag(tagName) != null) {
            return
        }
        dialogFragment.also {
            it.show(fragmentManager, tagName)
        }
    }

    override fun pushDialogFragmentToActivity(dialogFragment: DialogFragment, tagName: String) {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.findFragmentByTag(tagName) != null) {
            return
        }
        dialogFragment.also {
            it.show(fragmentManager, tagName)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tabStackManager?.onSaveInstanceState(outState)
        outState.putBoolean(KEY_APP_IS_KILLED, true)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Timber.d("onConfigurationChanged : $newConfig")
        // bit演算を行い、UI_MODE_NIGHT_MASKのみを取り出す
        when (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO,
            Configuration.UI_MODE_NIGHT_YES,
                -> actionCreator.keepAppTheme()

            else -> navigator.navigateToMainWithClearTop()
        }
        if (environmentConfig.isTablet) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    companion object {
        private const val EXTRA_FROM_SETUP = "from_setup"
        private const val KEY_APP_IS_KILLED = "app_is_killed"

        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java).also {
                it.putExtra(EXTRA_FROM_SETUP, true)
            }
        }

        fun createIntentWithClearTop(context: Context): Intent {
            return Intent(context, MainActivity::class.java).also {
                it.putExtra(EXTRA_FROM_SETUP, true)
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
            }
        }

        fun createIntent(context: Context, uri: Uri): Intent {
            return Intent(context, MainActivity::class.java).also {
                it.data = uri
            }
        }
    }
}
