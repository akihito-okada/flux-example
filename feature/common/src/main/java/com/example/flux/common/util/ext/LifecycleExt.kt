package com.example.flux.common.util.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.State.INITIALIZED
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.flux.model.util.CoroutinePlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

object LifecycleExt {

    /**
     * from: https://github.com/Kotlin/kotlinx.coroutines/pull/760
     * Modify to use [CoroutinePlugin] for testing
     * Returns a [CoroutineScope] that uses [Dispatchers.Main] by default, and that will be cancelled as
     * soon as this [Lifecycle] [currentState][Lifecycle.getCurrentState] is no longer
     * [at least][Lifecycle.State.isAtLeast] the passed [activeWhile] state.
     *
     * **Beware**: if the current state is lower than the passed [activeWhile] state, you'll get an
     * already cancelled scope.
     */
    fun Lifecycle.createScope(activeWhile: Lifecycle.State): CoroutineScope {
        return CoroutineScope(createJob(activeWhile) + Dispatchers.Main)
    }

    /**
     * Creates a [SupervisorJob] that will be cancelled as soon as this [Lifecycle]
     * [currentState][Lifecycle.getCurrentState] is no longer [at least][Lifecycle.State.isAtLeast] the
     * passed [activeWhile] state.
     *
     * **Beware**: if the current state is lower than the passed [activeWhile] state, you'll get an
     * already cancelled job.
     */
    fun Lifecycle.createJob(activeWhile: Lifecycle.State = INITIALIZED): Job {
        require(activeWhile != Lifecycle.State.DESTROYED) {
            "DESTROYED is a terminal state that is forbidden for createJob(…), to avoid leaks."
        }
        return SupervisorJob().also { job ->
            when (currentState) {
                Lifecycle.State.DESTROYED -> job.cancel() // Fast path if already destroyed
                else -> runCatching {
                    addObserver(
                        object : LifecycleEventObserver {
                            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                                if (!currentState.isAtLeast(activeWhile)) {
                                    removeObserver(this)
                                    job.cancel()
                                }
                            }
                        },
                    )
                }.onFailure {
                    // TestのときはStub!というエラーが出て落ちるため
                    Timber.e(it)
                }
            }
        }
    }
}
