package com.example.flux.common.util.bottomnavigation

import androidx.annotation.AnimRes
import androidx.annotation.StyleRes
import androidx.fragment.app.FragmentTransaction

class FragmentNavigationTransactionOptions private constructor(builder: Builder) {

    @TabStackManager.Transit
    internal var transition = FragmentTransaction.TRANSIT_NONE

    @AnimRes
    internal var enterAnimation = 0

    @AnimRes
    internal var exitAnimation = 0

    @AnimRes
    internal var popEnterAnimation = 0

    @AnimRes
    internal var popExitAnimation = 0

    @StyleRes
    internal var transitionStyle = 0
    internal var title: String? = null
    private var shortTitle: String? = null

    init {
        transition = builder.transition
        enterAnimation = builder.enterAnimation
        exitAnimation = builder.exitAnimation
        transitionStyle = builder.transitionStyle
        popEnterAnimation = builder.popEnterAnimation
        popExitAnimation = builder.popExitAnimation
        title = builder.breadCrumbTitle
        shortTitle = builder.breadCrumbShortTitle
    }

    class Builder {

        var transition: Int = 0
        var enterAnimation: Int = 0
        var exitAnimation: Int = 0
        var transitionStyle: Int = 0
        var popEnterAnimation: Int = 0
        var popExitAnimation: Int = 0
        var breadCrumbTitle: String? = null
        var breadCrumbShortTitle: String? = null

        private fun customAnimations(@AnimRes enterAnimation: Int, @AnimRes exitAnimation: Int): Builder {
            this.enterAnimation = enterAnimation
            this.exitAnimation = exitAnimation
            return this
        }

        fun customAnimations(@AnimRes enterAnimation: Int, @AnimRes exitAnimation: Int, @AnimRes popEnterAnimation: Int, @AnimRes popExitAnimation: Int): Builder {
            this.popEnterAnimation = popEnterAnimation
            this.popExitAnimation = popExitAnimation
            return customAnimations(enterAnimation, exitAnimation)
        }

        fun build(): FragmentNavigationTransactionOptions {
            return FragmentNavigationTransactionOptions(this)
        }
    }

    companion object {

        fun newBuilder(): Builder {
            return Builder()
        }
    }
}
