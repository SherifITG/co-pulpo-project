package com.itgates.co.pulpo.ultra.connectivityObserver

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<ConnectivityStatus>

    enum class ConnectivityStatus {
        AVAILABLE, UNAVAILABLE,  LOST, LOSING
    }
}