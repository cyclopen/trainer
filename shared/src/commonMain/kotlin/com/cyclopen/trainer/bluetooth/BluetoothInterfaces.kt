package com.cyclopen.trainer.bluetooth

import com.cyclopen.trainer.model.BtDevice
import com.cyclopen.trainer.model.Telemetry
import com.cyclopen.trainer.model.TrainerControl
import kotlinx.coroutines.flow.Flow

/**
 * Interface for Bluetooth device scanning
 */
interface BluetoothScanner {
    /**
     * Start scanning for Bluetooth devices
     * @return Flow of discovered devices
     */
    fun startScan(): Flow<BtDevice>
    
    /**
     * Stop scanning for devices
     */
    fun stopScan()
    
    /**
     * Check if Bluetooth is available and enabled
     */
    fun isBluetoothAvailable(): Boolean
    
    /**
     * Request necessary Bluetooth permissions (platform-specific)
     */
    suspend fun requestPermissions(): Boolean
}

/**
 * Interface for managing Bluetooth connections and data
 */
interface BluetoothConnection {
    /**
     * Connect to a specific device
     */
    suspend fun connect(device: BtDevice): Boolean
    
    /**
     * Disconnect from current device
     */
    suspend fun disconnect()
    
    /**
     * Observe telemetry data from connected device
     */
    fun observeTelemetry(): Flow<Telemetry>
    
    /**
     * Send control command to trainer
     */
    suspend fun sendControl(control: TrainerControl): Boolean
    
    /**
     * Get current connection status
     */
    fun isConnected(): Boolean
}

/**
 * Platform-specific factory for BluetoothScanner
 */
expect fun provideBluetoothScanner(): BluetoothScanner

/**
 * Platform-specific factory for BluetoothConnection
 */
expect fun provideBluetoothConnection(): BluetoothConnection
