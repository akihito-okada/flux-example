package com.example.flux.common.flux

import com.example.flux.model.util.CoroutinePlugin
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
 * From : https://github.com/DroidKaigi/conference-app-2019/blob/master/corecomponent/androidcomponent/src/main/java/io/github/droidkaigi/confsched2019/dispatcher/Dispatcher.kt
 */
@Singleton
class Dispatcher @Inject constructor(
    private val applicationCoroutineScope: CoroutineScope,
) {
    private val _actions = MutableSharedFlow<Action>()
    val events get() = _actions

    inline fun <reified T : Action> subscribe(): Flow<T> {
        return events.filterAndCast()
    }

    suspend fun dispatch(action: Action) {
        // Make sure calling `_actions.send()` from single thread. We can lose action if
        // `_actions.send()` is called simultaneously from multiple threads
        // https://github.com/Kotlin/kotlinx.coroutines/blob/1.0.1/common/kotlinx-coroutines-core-common/src/channels/ConflatedBroadcastChannel.kt#L227-L230
        withContext(CoroutinePlugin.mainDispatcher) {
            _actions.emit(action)
        }
    }

    fun launchAndDispatch(action: Action) {
        applicationCoroutineScope.launch(CoroutinePlugin.mainDispatcher) {
            _actions.emit(action)
        }
    }

    inline fun <reified E, reified R : E> Flow<E>.filterAndCast(
        context: CoroutineContext = Dispatchers.Unconfined,
    ): Flow<R> =
        channelFlow {
            collect { e ->
                (e as? R)?.let { r ->
                    trySend(r)
                }
            }
        }.flowOn(context)
}
