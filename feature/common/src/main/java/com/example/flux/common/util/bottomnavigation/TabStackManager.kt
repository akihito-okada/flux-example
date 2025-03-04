package com.example.flux.common.util.bottomnavigation

import android.os.Bundle
import androidx.annotation.CheckResult
import androidx.annotation.IdRes
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.flux.common.R
import com.example.flux.common.model.FragmentOptions
import java.util.*
import org.json.JSONArray
import timber.log.Timber

/**
 * Reference https://github.com/ncapdevi/FragNav/blob/master/frag-nav/src/main/java/com/ncapdevi/fragnav/FragNavController.java
 */

const val KEY_EXTRA_SELECTED_TAB_POSITION = "KEY_EXTRA_SELECTED_TAB_POSITION"
private const val KEY_EXTRA_TAG_COUNT = "KEY_EXTRA_TAG_COUNT"
private const val KEY_EXTRA_CURRENT_FRAGMENT = "KEY_EXTRA_CURRENT_FRAGMENT"
private const val KEY_EXTRA_FRAGMENT_STACK = "KEY_EXTRA_FRAGMENT_STACK"
private const val KEY_EXTRA_HORIZONTAL_FRAGMENT_STACK = "KEY_EXTRA_HORIZONTAL_FRAGMENT_STACK"

private const val INVALID_POSITION = -1

class TabStackManager private constructor(builder: Builder, savedInstanceState: Bundle?) {
    @IdRes
    private val containerId: Int

    @TabPosition
    var selectedTabPosition: Int = 0
    private val fragmentStacks: MutableList<Stack<Fragment>>
    private val fragmentManager: FragmentManager
    private var rootFragmentListener: RootFragmentListener? = null
    private var tabSelectedPositionManager: TabSelectedPositionManager
    private var destinationChangedListener: OnDestinationChangedListener? = null
    private var tagCount: Int = 0
    private var executingTransaction: Boolean = false

    private val defaultTransactionOptions =
        FragmentNavigationTransactionOptions.newBuilder().customAnimations(
            R.anim.anim_fade_in,
            R.anim.anim_fade_out,
            R.anim.anim_fade_in,
            R.anim.anim_fade_out,
        ).build()

    private val modalTransactionOptions =
        FragmentNavigationTransactionOptions.newBuilder().customAnimations(
            R.anim.anim_slide_in_up,
            R.anim.anim_fade_out,
            R.anim.anim_fade_in,
            R.anim.anim_slide_in_down,
        ).build()

    @Suppress("unused")
    val previousFragment: Fragment?
        get() {
            val fragments = fragmentStacks[selectedTabPosition]
            return if (fragments.size < 1) {
                null
            } else {
                fragments[fragments.size - 2]
            }
        }

    /**
     * Helper function to attempt to get current fragment
     * @return
     */
    // Attempt to used stored current fragment
    // If not, try to pull it from the stack
    val currentFragment: Fragment?
        @CheckResult
        get() {
            frontFragment?.also {
                return it
            }
            if (selectedTabPosition == INVALID_POSITION) {
                return null
            }
            val fragmentStack = fragmentStacks[selectedTabPosition]
            if (!fragmentStack.isEmpty()) {
                frontFragment =
                    fragmentManager.findFragmentByTag(fragmentStacks[selectedTabPosition].peek().tag)
            }
            return frontFragment
        }
    private var frontFragment: Fragment? = null

    val isHome get() = pageSize == 1

    private val pageSize: Int
        get() {
            var size = 0
            fragmentStacks.forEach {
                size += it.size
            }
            return size
        }

    /**
     * Get the number of fragment stacks
     *
     * @return the number of fragment stacks
     */
    val size: Int
        @CheckResult
        get() = fragmentStacks.size

    /**
     * Get a copy of the current stack that is being displayed
     *
     * @return Current stack
     */
    private val currentStack: Stack<Fragment>?
        get() = getStack(selectedTabPosition)

    /**
     * @return If true, you are at the bottom of the stack
     * (Consider using replaceFragment if you need to change the root fragment for some reason)
     * else you can popFragment as needed as your are not at the root
     */
    val isRootFragment: Boolean
        get() = currentStack?.size == 1

    val isEmptyTabPosition: Boolean
        get() = tabSelectedPositionManager.isEmptyTabPosition

    private val stackSizeTabPosition: Int
        get() = tabSelectedPositionManager.stackSizeTabPosition

    init {
        fragmentManager = builder.fragmentManager
        containerId = builder.containerId
        val bottomNavigationSize = rootFragmentListener?.bottomNavigationSize ?: 0
        fragmentStacks = ArrayList(bottomNavigationSize)
        rootFragmentListener = builder.rootFragmentListener
        destinationChangedListener = builder.destinationChangedListener

        selectedTabPosition = getDefaultBottomNavigation(savedInstanceState)
        tabSelectedPositionManager = TabSelectedPositionManagerImpl()

        // Restore from bundle, if not, initialize
        if (!restoreFromBundle(savedInstanceState)) {
            // StackをAdd
            val size = rootFragmentListener?.bottomNavigationSize ?: 0
            (0 until size).forEach { _ ->
                val stack = Stack<Fragment>()
                fragmentStacks.add(stack)
            }
            initialize(selectedTabPosition)
        }
    }

    private fun getDefaultBottomNavigation(savedInstanceState: Bundle?): Int {
        var defaultSelected = rootFragmentListener?.defaultBottomNavigationPosition ?: 0
        if (savedInstanceState != null) {
            defaultSelected = savedInstanceState.getInt(KEY_EXTRA_SELECTED_TAB_POSITION)
        }
        return defaultSelected
    }

    /**
     * Function used to switch to the specified fragment stack
     * @param position
     * @param transactionOptions
     */
    @Suppress("SameParameterValue")
    private fun switchTab(@TabPosition position: Int, transactionOptions: FragmentNavigationTransactionOptions?) {
        // Check to make sure the tab is within range
        if (position >= fragmentStacks.size) {
            return
        }
        // Return if already selected
        if (selectedTabPosition == position) {
            return
        }

        selectedTabPosition = position
        val fragmentTransaction = createTransactionWithOptions(transactionOptions)
        detachCurrentFragment(fragmentTransaction)

        var fragment: Fragment? = null
        if (position != INVALID_POSITION) {
            // Attempt to reattach previous fragment
            fragment = reattachPreviousFragment(fragmentTransaction) ?: run {
                return@run getRootFragment(selectedTabPosition).also {
                    fragmentTransaction.replace(containerId, it, generateTag(it))
                }
            }
        }

        fragmentTransaction.commit()

        executePendingTransactions()

        frontFragment = fragment

        fragment?.also {
            destinationChangedListener?.onDestinationChanged(it)
        }
    }

    /**
     * Function used to switch to the specified fragment stack
     * @param position
     */
    fun switchTab(position: Int) {
        pushTabPosition(position)
        switchTab(position, null)
    }

    fun popTab(): Int {
        var currentPosition = rootFragmentListener?.defaultBottomNavigationPosition ?: 0
        if (stackSizeTabPosition > STACK_SIZE_MIN) {
            currentPosition = popPreviousTabPosition()
        } else {
            clearStackTabPosition()
        }
        switchTab(currentPosition, null)
        return currentPosition
    }

    /**
     * Push a fragment onto the current stack
     * @param fragment
     * @param transactionOptions
     */
    private fun pushFragment(
        fragment: Fragment?,
        transactionOptions: FragmentNavigationTransactionOptions?,
    ) {
        fragment ?: return
        if (selectedTabPosition == INVALID_POSITION) {
            return
        }
        createTransactionWithOptions(transactionOptions).also {
            detachCurrentFragment(it)
            it.add(containerId, fragment, generateTag(fragment))
            it.commit()
        }

        executePendingTransactions()

        fragmentStacks[selectedTabPosition].push(fragment)

        frontFragment = fragment

        destinationChangedListener?.onDestinationChanged(fragment)
    }

    /**
     * Push a fragment onto the current stack
     * @param fragment
     */
    fun pushFragment(fragment: Fragment?) {
        pushFragment(fragment, null)
    }

    fun pushFragmentWithModal(fragment: Fragment?) {
        pushFragment(fragment, modalTransactionOptions)
    }

    /**
     * Pop the current fragment from the current tab
     * @throws UnsupportedOperationException
     */
    @Throws(UnsupportedOperationException::class)
    private fun popFragment(transactionOptions: FragmentNavigationTransactionOptions?) {
        popFragments(1, transactionOptions)
    }

    /**
     * Pop the current fragment from the current tab
     */
    @Throws(UnsupportedOperationException::class)
    fun popFragment() {
        popFragment(null)
    }

    @Throws(UnsupportedOperationException::class)
    fun popFragmentWithModal() {
        popFragment(modalTransactionOptions)
    }

    /**
     * Pop the current fragment from the current tab
     */
    @Throws(UnsupportedOperationException::class)
    fun popFragment(popDepth: Int) {
        popFragments(popDepth, null)
    }

    /**
     * Pop the current stack until a given tag is found. If the tag is not found, the stack will popFragment until it is at
     * the root fragment
     *
     * @param transactionOptions Transaction options to be displayed
     */
    @Throws(UnsupportedOperationException::class)
    private fun popFragments(
        popDepth: Int,
        transactionOptions: FragmentNavigationTransactionOptions?,
    ) {
        // java.lang.IllegalStateException: Can not perform this action after onSaveInstanceStateがでていたので入れてみる
        // Need to have this down here so that that tag has been
        // committed to the fragment before we add to the stack
        // java.lang.IllegalStateException: Can not perform this action after onSaveInstanceStateがでていたので入れてみる
        // java.lang.IllegalStateException: Can not perform this action after onSaveInstanceStateがでていたので入れてみる
        when {
            isRootFragment -> {
                throw UnsupportedOperationException("You can not popFragment the rootFragment. If you need to change this fragment, use replaceFragment(fragment)")
            }

            popDepth < 1 -> {
                throw UnsupportedOperationException("popFragments parameter needs to be greater than 0")
            }

            selectedTabPosition == INVALID_POSITION -> {
                throw UnsupportedOperationException("You can not pop fragments when no tab is selected")
            }

            popDepth >= fragmentStacks[selectedTabPosition].size - 1 -> {
                // If our popDepth is big enough that it would just clear the stack, then call that.
                clearStack(transactionOptions, null)
                return
            }

            else -> {
                // Pop the number of the fragments on the stack and remove them from the FragmentManager
                // Attempt to reattach previous fragment
                // If we can't reattach, either pull from the stack, or create a new root fragment

                var fragment: Fragment?
                val fragmentTransaction = createTransactionWithOptions(transactionOptions)

                // Pop the number of the fragments on the stack and remove them from the FragmentManager
                for (i in 0 until popDepth) {
                    fragment =
                        fragmentManager.findFragmentByTag(fragmentStacks[selectedTabPosition].pop().tag)
                    fragment?.also {
                        fragmentTransaction.remove(it)
                    }
                }

                // Attempt to reattach previous fragment
                fragment = reattachPreviousFragment(fragmentTransaction)

                var shouldPush = false
                // If we can't reattach, either pull from the stack, or create a new root fragment
                if (fragment != null) {
                    // java.lang.IllegalStateException: Can not perform this action after onSaveInstanceStateがでていたので入れてみる
                    if (fragment.isRemoving) {
                        return
                    }
                    fragmentTransaction.commit()
                } else {
                    if (!fragmentStacks[selectedTabPosition].isEmpty()) {
                        fragment = fragmentStacks[selectedTabPosition].peek()
                        // java.lang.IllegalStateException: Can not perform this action after onSaveInstanceStateがでていたので入れてみる
                        if (fragment?.isRemoving == true) {
                            return
                        }
                        fragmentTransaction.replace(containerId, fragment, fragment.tag)
                        fragmentTransaction.commit()
                    } else {
                        fragment = getRootFragment(selectedTabPosition)
                        // java.lang.IllegalStateException: Can not perform this action after onSaveInstanceStateがでていたので入れてみる
                        if (fragment.isRemoving) {
                            return
                        }
                        fragmentTransaction.replace(containerId, fragment, generateTag(fragment))
                        fragmentTransaction.commit()

                        shouldPush = true
                    }
                }

                executePendingTransactions()

                // Need to have this down here so that that tag has been
                // committed to the fragment before we add to the stack
                if (shouldPush) {
                    fragmentStacks[selectedTabPosition].push(fragment)
                }

                frontFragment = fragment

                fragment?.also {
                    destinationChangedListener?.onDestinationChanged(it)
                }
            }
        }
    }

    /**
     * Clears the current tab's stack to get to just the bottom Fragment. This will reveal the root fragment
     * @param transactionOptions
     * @param onTabReselectedListener
     */
    private fun clearStack(
        transactionOptions: FragmentNavigationTransactionOptions?,
        onTabReselectedListener: OnTabReselectedListener?,
    ) {
        if (selectedTabPosition == INVALID_POSITION) {
            return
        }

        // Grab Current stack
        val fragmentStack = fragmentStacks[selectedTabPosition]

        // Only need to start popping and reattach if the stack is greater than 1
        if (fragmentStack.size <= 1) {
            currentFragment?.also {
                onTabReselectedListener?.onScrollToTop(it)
            }
            return
        }
        var fragment: Fragment?
        val fragmentTransaction = createTransactionWithOptions(transactionOptions)

        // Pop all of the fragments on the stack and remove them from the FragmentManager
        while (fragmentStack.size > 1) {
            fragment = fragmentManager.findFragmentByTag(fragmentStack.pop().tag)
            fragment?.also {
                fragmentTransaction.remove(it)
            }
        }

        // Attempt to reattach previous fragment
        fragment = reattachPreviousFragment(fragmentTransaction)

        var shouldPush = false
        // If we can't reattach, either pull from the stack, or create a new root fragment
        if (fragment != null) {
            // java.lang.IllegalStateException: Can not perform this action after onSaveInstanceStateがでていたので入れてみる
            if (fragment.isRemoving) {
                return
            }
            fragmentTransaction.commit()
        } else {
            if (!fragmentStack.isEmpty()) {
                fragment = fragmentStack.peek()
                // java.lang.IllegalStateException: Can not perform this action after onSaveInstanceStateがでていたので入れてみる
                if (fragment.isRemoving) {
                    return
                }
                fragmentTransaction.replace(containerId, fragment, fragment.tag)
                fragmentTransaction.commit()
            } else {
                fragment = getRootFragment(selectedTabPosition)
                // java.lang.IllegalStateException: Can not perform this action after onSaveInstanceStateがでていたので入れてみる
                if (fragment.isRemoving) {
                    return
                }
                fragmentTransaction.replace(containerId, fragment, generateTag(fragment))
                fragmentTransaction.commit()

                shouldPush = true
            }
        }

        executePendingTransactions()

        if (shouldPush) {
            fragmentStacks[selectedTabPosition].push(fragment)
        }
        // Update the stored version we have in the list
        fragmentStacks[selectedTabPosition] = fragmentStack

        frontFragment = fragment

        frontFragment?.also {
            onTabReselectedListener?.onClearStack(it)
        }

        fragment?.also {
            destinationChangedListener?.onDestinationChanged(it)
        }
    }

    /**
     * Clears the current tab's stack to get to just the bottom Fragment. This will reveal the root fragment.
     */
    fun clearStack(onTabReselectedListener: OnTabReselectedListener) {
        clearStack(null, onTabReselectedListener)
    }

    /**
     * Helper function to make sure that we are starting with a clean slate and to perform our first fragment interaction.
     *
     * @param position the tab position to initialize to
     */
    private fun initialize(@TabPosition position: Int) {
        selectedTabPosition = position
        if (selectedTabPosition > fragmentStacks.size) {
            throw IndexOutOfBoundsException("Starting position cannot be larger than the number of stacks")
        }
        clearFragmentManager()

        if (position == INVALID_POSITION) {
            return
        }

        val ft = createTransactionWithOptions(null)

        val fragment = getRootFragment(position)
        ft.replace(containerId, fragment, generateTag(fragment))
        ft.commit()

        executePendingTransactions()

        frontFragment = fragment

        fragment.also {
            destinationChangedListener?.onDestinationChanged(it)
        }
    }

    /**
     * Helper function to get the root fragment for a given position. This is done by either passing them in the constructor, or dynamically via NavListener.
     *
     * @param position The tab position to get this fragment from
     *
     * @return The root fragment at this position
     *
     * @throws IllegalStateException This will be thrown if we can't find a rootFragment for this position. Either because you didn't provide it in the
     * constructor, or because your RootFragmentListener.getRootFragment(position) isn't returning a fragment for this position.
     */
    @CheckResult
    @Throws(IllegalStateException::class)
    private fun getRootFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        if (!fragmentStacks[position].isEmpty()) {
            fragment = fragmentStacks[position].peek()
        } else if (rootFragmentListener != null) {
            fragment = rootFragmentListener?.getRootFragment(position)
            if (selectedTabPosition != INVALID_POSITION) {
                fragment?.also {
                    fragmentStacks[selectedTabPosition].push(it)
                }
            }
        }
        fragment
            ?: throw IllegalStateException(
                "Either you haven't past in a fragment at this position in your constructor, or you haven't " + "provided a way to create it while via your RootFragmentListener.getRootFragment(position)",
            )
        return fragment
    }

    /**
     * Will attempt to reattach a previous fragment in the FragmentManager, or return null if not able to.
     *
     * @param ft current fragment transaction
     *
     * @return Fragment if we were able to find and reattach it
     */
    private fun reattachPreviousFragment(ft: FragmentTransaction): Fragment? {
        val fragmentStack = fragmentStacks[selectedTabPosition]
        var fragment: Fragment? = null
        if (!fragmentStack.isEmpty()) {
            fragment = fragmentManager.findFragmentByTag(fragmentStack.peek().tag)
            fragment?.also {
                ft.attach(it)
            }
        }
        return fragment
    }

    /**
     * Attempts to detach any current fragment if it exists, and if none is found, returns.
     *
     * @param fragmentTransaction the current transaction being performed
     */
    private fun detachCurrentFragment(fragmentTransaction: FragmentTransaction) {
        val oldFragment = currentFragment
        oldFragment?.also {
            fragmentTransaction.detach(it)
        }
    }

    /**
     * Create a unique fragment tag so that we can grab the fragment later from the FragmentManger
     * @param fragment
     * @return
     */
    @CheckResult
    private fun generateTag(fragment: Fragment): String {
        return fragment.javaClass.name + ++tagCount
    }

    /**
     * This check is here to prevent recursive entries into executePendingTransactions
     */
    private fun executePendingTransactions() {
        if (executingTransaction) {
            return
        }
        executingTransaction = true
        try {
            fragmentManager.executePendingTransactions()
        } catch (e: Exception) {
            Timber.d(e)
        }

        executingTransaction = false
    }

    /**
     * Private helper function to clear out the fragment manager on initialization. All fragment management should be done via FragNav.
     */
    private fun clearFragmentManager() {
        val fragmentTransaction = createTransactionWithOptions(null)
        for (fragment in fragmentManager.fragments) {
            if (fragment != null) {
                fragmentTransaction.remove(fragment)
            }
        }
        fragmentTransaction.commit()
        executePendingTransactions()
    }

    /**
     * Setup a fragment transaction with the given option
     *
     * @param transactionOptions The options that will be set for this transaction
     */
    @CheckResult
    private fun createTransactionWithOptions(transactionOptions: FragmentNavigationTransactionOptions?): FragmentTransaction {
        return fragmentManager.beginTransaction().apply {
            setCustomAnimations(this, transactionOptions ?: defaultTransactionOptions)
            setReorderingAllowed(true)
        }
    }

    private fun setCustomAnimations(
        fragmentTransaction: FragmentTransaction,
        transactionOptions: FragmentNavigationTransactionOptions?,
    ) {
        transactionOptions ?: return
        fragmentTransaction.setCustomAnimations(
            transactionOptions.enterAnimation,
            transactionOptions.exitAnimation,
            transactionOptions.popEnterAnimation,
            transactionOptions.popExitAnimation,
        )
    }

    /**
     * Get a copy of the stack at a given position
     *
     * @return requested stack
     */
    private fun getStack(@TabPosition position: Int): Stack<Fragment>? {
        if (position >= fragmentStacks.size) {
            throw IndexOutOfBoundsException("Can't get an position that's larger than we've setup")
        }
        return fragmentStacks[position]
    }

    /**
     * Call this in your Activity's onSaveInstanceState(Bundle outState) method to save the instance's state.
     *
     * @param outState The Bundle to save state information to
     */
    fun onSaveInstanceState(outState: Bundle) {
        // Write tag count
        outState.putInt(KEY_EXTRA_TAG_COUNT, tagCount)

        outState.putSerializable(KEY_EXTRA_HORIZONTAL_FRAGMENT_STACK, tabSelectedPositionManager)

        // Write select tab
        outState.putInt(KEY_EXTRA_SELECTED_TAB_POSITION, selectedTabPosition)

        // Write current fragment
        frontFragment?.also {
            FragmentOptions.fromArguments(it.arguments).toArguments(outState)
            outState.putString(KEY_EXTRA_CURRENT_FRAGMENT, it.tag)
        }
        // Write stacks
        try {
            val stackArrays = JSONArray()

            for (stack in fragmentStacks) {
                val stackArray = JSONArray()

                for (fragment in stack) {
                    stackArray.put(fragment.tag)
                }

                stackArrays.put(stackArray)
            }

            outState.putString(KEY_EXTRA_FRAGMENT_STACK, stackArrays.toString())
        } catch (t: Throwable) {
            // Nothing we can do
        }
    }

    /**
     * Restores this instance to the state specified by the contents of savedInstanceState
     *
     * @param savedInstanceState The bundle to restore from
     *
     * @return true if successful, false if not
     */
    private fun restoreFromBundle(savedInstanceState: Bundle?): Boolean {
        savedInstanceState ?: return false

        val tabSelectedPositionManager = savedInstanceState.getSerializable(KEY_EXTRA_HORIZONTAL_FRAGMENT_STACK) as? TabSelectedPositionManager

        tabSelectedPositionManager ?: return false

        this.tabSelectedPositionManager = tabSelectedPositionManager

        // Restore tag count
        tagCount = savedInstanceState.getInt(KEY_EXTRA_TAG_COUNT, 0)

        // Restore current fragment
        frontFragment = fragmentManager.findFragmentByTag(
            savedInstanceState.getString(
                KEY_EXTRA_CURRENT_FRAGMENT,
            ),
        )

        // Restore fragment stacks
        try {
            val stackArrays = JSONArray(savedInstanceState.getString(KEY_EXTRA_FRAGMENT_STACK))

            for (i in 0 until stackArrays.length()) {
                val stackArray = stackArrays.getJSONArray(i)
                val stack = Stack<Fragment>()

                if (stackArray.length() == 1) {
                    val tag = stackArray.getString(0)
                    val fragment: Fragment?

                    fragment = if (tag.isNullOrEmpty()) {
                        getRootFragment(i)
                    } else {
                        fragmentManager.findFragmentByTag(tag)
                    }
                    fragment?.also {
                        stack.add(it)
                    }
                } else {
                    for (j in 0 until stackArray.length()) {
                        val tag = stackArray.getString(j)
                        if (tag.isNotEmpty()) {
                            val fragment = fragmentManager.findFragmentByTag(tag)
                            if (fragment != null) {
                                stack.add(fragment)
                            }
                        }
                    }
                }
                fragmentStacks.add(stack)
            }

            // Restore selected tab if we have one
            switchTab(getDefaultBottomNavigation(savedInstanceState), null)

            // Restore frontFragment's options
            frontFragment?.also {
                destinationChangedListener?.onDestinationChanged(it)
            }
            // Successfully restored state
            return true
        } catch (t: Throwable) {
            return false
        }
    }

    private fun pushTabPosition(entry: Int) {
        tabSelectedPositionManager.pushTabPosition(entry)
    }

    private fun clearStackTabPosition() {
        tabSelectedPositionManager.clearStackTabPosition()
    }

    private fun popPreviousTabPosition(): Int {
        return tabSelectedPositionManager.popPreviousTabPosition()
    }

    fun destroy() {
        fragmentStacks.clear()
        tabSelectedPositionManager.destroy()
        frontFragment = null
        rootFragmentListener = null
    }

    @Retention
    private annotation class TabPosition

    // Declare Transit Styles
    @IntDef(
        FragmentTransaction.TRANSIT_NONE,
        FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
        FragmentTransaction.TRANSIT_FRAGMENT_CLOSE,
        FragmentTransaction.TRANSIT_FRAGMENT_FADE,
    )
    @Retention
    internal annotation class Transit

    interface RootFragmentListener {
        val defaultBottomNavigationPosition: Int
        val bottomNavigationSize: Int
        fun getRootFragment(position: Int): Fragment
    }

    class Builder(
        private val savedInstanceState: Bundle?,
        val fragmentManager: FragmentManager,
        val containerId: Int,
    ) {
        var rootFragmentListener: RootFragmentListener? = null

        var destinationChangedListener: OnDestinationChangedListener? = null

        private val mRootFragments: List<Fragment>? = null

        /**
         * @param rootFragmentListener a listener that allows for dynamically creating root fragments
         */
        fun rootFragmentListener(rootFragmentListener: RootFragmentListener): Builder {
            this.rootFragmentListener = rootFragmentListener
            return this
        }

        fun destinationChangedListener(destinationChangedListener: OnDestinationChangedListener): Builder {
            this.destinationChangedListener = destinationChangedListener
            return this
        }

        fun build(): TabStackManager {
            if (rootFragmentListener == null && mRootFragments == null) {
                throw IndexOutOfBoundsException("Either a root fragment(s) needs to be set, or a fragment listener")
            }
            return TabStackManager(this, savedInstanceState)
        }
    }

    companion object {

        fun newBuilder(
            savedInstanceState: Bundle?,
            fragmentManager: FragmentManager,
            containerId: Int,
        ): Builder {
            return Builder(
                savedInstanceState,
                fragmentManager,
                containerId,
            )
        }
    }

    interface OnTabReselectedListener {
        fun onScrollToTop(fragment: Fragment)
        fun onClearStack(fragment: Fragment)
    }

    interface OnDestinationChangedListener {
        fun onDestinationChanged(
            fragment: Fragment,
        )
    }
}
