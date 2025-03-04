/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.flux.common.util.ext

/**
 * Extension functions for View and subclasses of View.
 */

import android.annotation.SuppressLint
import android.transition.TransitionManager
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.flux.common.model.SnackBarActionType
import com.google.android.material.snackbar.Snackbar

private const val CLICKABLE_DELAY_TIME = 300L

object ViewExt {

    fun View.toGone() {
        visibility = View.GONE
    }

    fun View.toInvisible() {
        visibility = View.INVISIBLE
    }

    fun View.toVisible() {
        visibility = View.VISIBLE
    }

    fun View.isVisible(): Boolean {
        return visibility == View.VISIBLE
    }

    fun View.isGone(): Boolean {
        return visibility == View.GONE
    }

    fun View.isInvisible(): Boolean {
        return visibility == View.INVISIBLE
    }

    @SuppressLint("ClickableViewAccessibility")
    fun View.preventTouch() {
        setOnTouchListener { _, _ ->
            true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun View.cancelPreventTouch() {
        setOnTouchListener { _, _ ->
            false
        }
    }

    fun ViewGroup.toGoneWithDelayedTransition() {
        if (visibility == View.GONE) {
            return
        }
        TransitionManager.beginDelayedTransition(this)
        visibility = View.GONE
    }

    fun ViewGroup.toVisibleWithDelayedTransition() {
        if (visibility == View.VISIBLE) {
            return
        }
        TransitionManager.beginDelayedTransition(this)
        visibility = View.VISIBLE
    }

    fun View.fadeOut(onCompleted: (() -> Unit)? = null) {
        if (visibility == View.GONE) {
            onCompleted?.invoke()
            return
        }
        val animation = AlphaAnimation(1.0f, 0.0f).also {
            it.duration = 200
            it.fillAfter = true
            it.setAnimationListener(
                object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        toGone()
                        onCompleted?.invoke()
                    }

                    override fun onAnimationStart(animation: Animation?) {}
                },
            )
        }
        startAnimation(animation)
    }

    fun View.fadeIn(duration: Long = 200) {
        if (visibility == View.VISIBLE) {
            return
        }
        val animation = AlphaAnimation(0.0f, 1.0f).also {
            it.duration = duration
            it.setAnimationListener(
                object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                    }

                    override fun onAnimationStart(animation: Animation?) {
                        toVisible()
                    }
                },
            )
        }
        startAnimation(animation)
    }

    /**
     * Transforms static java function Snackbar.make() to an extension function on View.
     */
    fun View.showSnackbar(
        snackbarText: String,
        timeLength: Int,
        type: SnackBarActionType = SnackBarActionType.None,
        callback: (() -> Unit)? = null,
        actionCallback: ((SnackBarActionType) -> Unit)? = null,
    ): Snackbar {
        val snackbar = Snackbar.make(this, snackbarText, timeLength)
        snackbar.addCallback(
            object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    callback?.invoke()
                }
            },
        )
        if (type.isNone.not()) {
            snackbar.setAction(type.actionName(resources)) {
                actionCallback?.invoke(type)
            }
        }
        snackbar.show()
        return snackbar
    }

    /**
     * Extension function for [View.setOnClickListener].
     * It prevents fast clicking by user.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : View> T.setSafeClickListener(delayTime: Long, listener: ((it: T) -> Unit)?) {
        listener ?: run {
            setOnClickListener(null)
            isClickable = false
            return
        }
        setOnClickListener { view ->
            view ?: return@setOnClickListener
            view.isEnabled = false

            postDelayed(
                { view.isEnabled = true },
                delayTime,
            )

            listener.invoke(view as T)
        }
    }

    fun <T : View> T.setSafeClickListener(listener: ((it: T) -> Unit)?) {
        setSafeClickListener(CLICKABLE_DELAY_TIME, listener)
    }

    fun View.setStaggeredGridLayoutFullSpan() {
        val layoutParams = layoutParams as? StaggeredGridLayoutManager.LayoutParams
        layoutParams?.isFullSpan = true
    }

    fun View.performHapticFeedback() {
        performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
    }
}
