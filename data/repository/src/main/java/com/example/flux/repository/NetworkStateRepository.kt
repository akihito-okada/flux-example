package com.example.flux.repository

import kotlinx.coroutines.flow.Flow

interface NetworkStateRepository {
    val isOnline: Boolean
    val isOverWifi: Boolean
    val isOverCellular: Boolean
    val isOverEthernet: Boolean
    fun watchNetwork(): Flow<Boolean>
    fun watchWifi(): Flow<Boolean>
    fun watchCellular(): Flow<Boolean>
    fun watchEthernet(): Flow<Boolean>
}
