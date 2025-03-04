package com.example.flux.common.util.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.flux.common.util.bottomnavigation.FragmentNavigator
import com.example.flux.common.model.FragmentOptions
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

object FragmentExt {

    fun Fragment.switchTab(position: Int) {
        (activity as? FragmentNavigator)?.switchTab(position)
    }

    fun Fragment.pushFragment(fragment: Fragment) {
        (activity as? FragmentNavigator)?.pushFragment(fragment)
    }

    fun Fragment.pushFragmentWithModal(fragment: Fragment) {
        (activity as? FragmentNavigator)?.pushFragmentWithModal(fragment)
    }

    fun Fragment.popFragment() {
        (activity as? FragmentNavigator)?.popFragment()
    }

    fun Fragment.popFragmentWithModal() {
        (activity as? FragmentNavigator)?.popFragment()
    }

    fun Fragment.popFragment(popDepth: Int) {
        (activity as? FragmentNavigator)?.popFragment(popDepth)
    }

    fun Fragment.clearStack() {
        (activity as? FragmentNavigator)?.clearStack()
    }

    fun Fragment.popSwitchTab() {
        (activity as? FragmentNavigator)?.popSwitchTab()
    }

    val Fragment.fragmentOptions get() = FragmentOptions.fromArguments(arguments)

    fun Fragment.mainThreadPost(invoke: () -> Unit) {
        view?.post {
            viewLifecycleOwnerLiveData.value?.mainThreadPost(invoke)
        }
    }

    fun Fragment.mainThreadPostDelayed(invoke: (() -> Unit)?, delayTime: Long) {
        invoke ?: return
        viewLifecycleOwnerLiveData.value?.mainThreadPostDelayed(invoke, delayTime)
    }

    private fun LifecycleOwner.mainThreadPost(invoke: () -> Unit) {
        lifecycleScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                Timber.d(throwable)
            },
        ) {
            invoke()
        }
    }

    private fun LifecycleOwner.mainThreadPostDelayed(invoke: () -> Unit, delayTime: Long) {
        lifecycleScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                Timber.d(throwable)
            },
        ) {
            delay(delayTime)
            invoke()
        }
    }

    fun Fragment.repeatViewLifecycleOnResumed(onResumed: () -> Unit) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                onResumed.invoke()
            }
        }
    }
}
