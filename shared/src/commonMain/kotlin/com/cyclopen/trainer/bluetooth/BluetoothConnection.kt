package com.cyclopen.trainer.bluetooth

import com.cyclopen.trainer.model.Telemetry
import com.cyclopen.trainer.model.TrainerControl
import kotlinx.coroutines.flow.Flow

/**
 * Interface for connecting to and communicating with a Bluetooth device.
 */
interface BluetoothConnection {
    /**
     * Connect to a device by its address.
     */
    suspend fun connect(deviceAddress: String): Result<Unit>
    
    /**
     * Disconnect from the current device.
     */
    suspend fun disconnect()
    
    /**
     * Observe telemetry data from the connected device.
     */
    fun observeTelemetry(): Flow<Telemetry>
    
    /**
     * Send a control command to a smart trainer.
     */
    suspend fun sendControl(command: TrainerControl): Result<Unit>
    
    /**
     * Check if currently connected.
     */
    fun isConnected(): Boolean
}
