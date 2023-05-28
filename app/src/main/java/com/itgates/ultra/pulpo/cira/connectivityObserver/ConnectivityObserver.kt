package com.itgates.ultra.pulpo.cira.connectivityObserver

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<ConnectivityStatus>

    enum class ConnectivityStatus {
        AVAILABLE, UNAVAILABLE,  LOST, LOSING
    }
}