package com.example.flux.common.flux

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flux.common.util.lifecycle.Event
import com.example.flux.model.util.CoroutinePlugin
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber

open class Store : ViewModel() {

    val ignoreHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.d(throwable)
    }

    private val onClearHooks: MutableList<() -> Unit> = mutableListOf()

    fun addHook(hook: () -> Unit) {
        onClearHooks.add(hook)
    }

    override fun onCleared() {
        super.onCleared()
        onClearHooks.forEach { it() }
    }

    @MainThread
    fun <T> Flow<T>.toLiveData(
        store: Store,
        defaultValue: T? = null,
        coroutineScope: CoroutineScope = viewModelScope,
    ): LiveData<T> {
        return object : LiveData<T>(), CoroutineScope by coroutineScope {

            private var job: Job? = null

            init {
                if (defaultValue != null) {
                    value = defaultValue
                }
                store.addHook {
                    job?.cancel()
                    job = null
                }
                connectLiveData()
            }

            private fun connectLiveData() {
                job = launch(CoroutinePlugin.mainDispatcher) {
                    this@toLiveData.collect { element ->
                        postValue(element)
                    }
                }
            }

            override fun onActive() {
                super.onActive()
                // onClearedが呼ばれたあとでもLiveDataは生きていることがあるので
                // jobが破棄されていたら作り直す
                // Reference : https://www.yslibrary.net/2019/04/04/kotlin-channel-and-android-lifecycle/
                job ?: run {
                    connectLiveData()
                }
            }
        }
    }

    @MainThread
    fun <T> Flow<T>.toLiveDataWithEvent(
        store: Store,
        defaultValue: T? = null,
        coroutineScope: CoroutineScope = viewModelScope,
    ): LiveData<Event<T>> {
        return object : LiveData<Event<T>>(), CoroutineScope by coroutineScope {

            private var job: Job? = null

            init {
                if (defaultValue != null) {
                    value = Event(defaultValue)
                }
                store.addHook {
                    job?.cancel()
                    job = null
                }
                connectLiveData()
            }

            private fun connectLiveData() {
                job = launch(CoroutinePlugin.mainDispatcher) {
                    collect { element ->
                        postValue(Event(element))
                    }
                }
            }

            override fun onActive() {
                super.onActive()
                // onClearedが呼ばれたあとでもLiveDataは生きていることがあるので
                // jobが破棄されていたら作り直す
                // Reference : https://www.yslibrary.net/2019/04/04/kotlin-channel-and-android-lifecycle/
                job ?: run {
                    connectLiveData()
                }
            }
        }
    }

    @MainThread
    fun <T> Flow<T>.toLiveDataBySet(
        store: Store,
        defaultValue: T? = null,
        coroutineScope: CoroutineScope = viewModelScope,
    ): LiveData<T> {
        return object : LiveData<T>(), CoroutineScope by coroutineScope {

            private var job: Job? = null

            init {
                if (defaultValue != null) {
                    value = defaultValue
                }
                store.addHook {
                    job?.cancel()
                    job = null
                }
                connectLiveData()
            }

            private fun connectLiveData() {
                job = launch(CoroutinePlugin.mainDispatcher) {
                    collect { element ->
                        value = element
                    }
                }
            }

            override fun onActive() {
                super.onActive()
                // onClearedが呼ばれたあとでもLiveDataは生きていることがあるので
                // jobが破棄されていたら作り直す
                // Reference : https://www.yslibrary.net/2019/04/04/kotlin-channel-and-android-lifecycle/
                job ?: run {
                    connectLiveData()
                }
            }
        }
    }

    @MainThread
    fun <T> Flow<T>.toLiveDataBySetWithEvent(
        store: Store,
        defaultValue: T? = null,
        coroutineScope: CoroutineScope = viewModelScope,
    ): LiveData<Event<T>> {
        return object : LiveData<Event<T>>(), CoroutineScope by coroutineScope {

            private var job: Job? = null

            init {
                if (defaultValue != null) {
                    value = Event(defaultValue)
                }
                store.addHook {
                    job?.cancel()
                    job = null
                }
                connectLiveData()
            }

            private fun connectLiveData() {
                job = launch(CoroutinePlugin.mainDispatcher) {
                    collect { element ->
                        value = Event(element)
                    }
                }
            }

            override fun onActive() {
                super.onActive()
                // onClearedが呼ばれたあとでもLiveDataは生きていることがあるので
                // jobが破棄されていたら作り直す
                // Reference : https://www.yslibrary.net/2019/04/04/kotlin-channel-and-android-lifecycle/
                job ?: run {
                    connectLiveData()
                }
            }
        }
    }
}
