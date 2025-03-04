package com.example.flux.model.util

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

/*
 * From : https://github.com/DroidKaigi/conference-app-2019/blob/master/corecomponent/androidcomponent/src/main/java/io/github/droidkaigi/confsched2019/ext/CoroutinePlugin.kt
 *
 */
object CoroutinePlugin {

    private val defaultIoDispatcher: CoroutineContext = Dispatchers.IO
    val ioDispatcher: CoroutineContext
        get() = ioDispatcherHandler?.invoke(defaultIoDispatcher) ?: defaultIoDispatcher

    var ioDispatcherHandler: ((CoroutineContext) -> CoroutineContext)? = null

    private val defaultComputationDispatcher: CoroutineContext = Dispatchers.Default
    val defaultDispatcher: CoroutineContext
        get() = computationDispatcherHandler?.invoke(defaultComputationDispatcher)
            ?: defaultComputationDispatcher

    var computationDispatcherHandler: ((CoroutineContext) -> CoroutineContext)? = null

    private val defaultMainDispatcher: CoroutineContext = Dispatchers.Main
    val mainDispatcher: CoroutineContext
        get() = mainDispatcherHandler?.invoke(defaultMainDispatcher) ?: defaultMainDispatcher

    var mainDispatcherHandler: ((CoroutineContext) -> CoroutineContext)? = null

    @JvmStatic
    fun reset() {
        ioDispatcherHandler = null
        computationDispatcherHandler = null
        mainDispatcherHandler = null
    }
}
