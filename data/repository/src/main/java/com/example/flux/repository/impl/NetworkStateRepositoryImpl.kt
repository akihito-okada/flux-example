package com.example.flux.repository.impl

import com.example.flux.remote.NetworkWatcher
import com.example.flux.repository.NetworkStateRepository
import kotlinx.coroutines.flow.Flow

class NetworkStateRepositoryImpl(
    private val networkWatcher: NetworkWatcher,
) : NetworkStateRepository {

    override val isOnline: Boolean get() = networkWatcher.isOnline
    override val isOverWifi: Boolean get() = networkWatcher.isOverWifi
    override val isOverCellular: Boolean get() = networkWatcher.isOverCellular
    override val isOverEthernet: Boolean get() = networkWatcher.isOverEthernet

    override fun watchNetwork(): Flow<Boolean> {
        return networkWatcher.watchNetwork()
    }

    override fun watchWifi(): Flow<Boolean> {
        return networkWatcher.watchWifi()
    }

    override fun watchCellular(): Flow<Boolean> {
        return networkWatcher.watchCellular()
    }

    override fun watchEthernet(): Flow<Boolean> {
        return networkWatcher.watchEthernet()
    }
}
