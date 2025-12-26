package com.cyclopen.trainer.bluetooth

import com.cyclopen.trainer.model.Telemetry
import com.cyclopen.trainer.model.TrainerControl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Stub implementation for Linux - Bluetooth not supported yet.
 */
class StubBluetoothConnection : BluetoothConnection {
    override suspend fun connect(deviceAddress: String): Result<Unit> {
        return Result.failure(UnsupportedOperationException("Bluetooth not supported on Linux"))
    }
    
    override suspend fun disconnect() {
        // No-op
    }
    
    override fun observeTelemetry(): Flow<Telemetry> = flowOf()
    
    override suspend fun sendControl(command: TrainerControl): Result<Unit> {
        return Result.failure(UnsupportedOperationException("Bluetooth not supported on Linux"))
    }
    
    override fun isConnected(): Boolean = false
}
