package com.cyclopen.trainer.bluetooth

import com.cyclopen.trainer.model.BtDevice
import kotlinx.coroutines.flow.Flow

/**
 * Interface for scanning Bluetooth devices.
 */
interface BluetoothScanner {
    /**
     * Start scanning for BLE devices.
     * Returns a flow of discovered devices.
     */
    fun scanDevices(): Flow<BtDevice>
    
    /**
     * Stop scanning for devices.
     */
    fun stopScan()
    
    /**
     * Check if Bluetooth is supported on this platform.
     */
    fun isSupported(): Boolean
}
