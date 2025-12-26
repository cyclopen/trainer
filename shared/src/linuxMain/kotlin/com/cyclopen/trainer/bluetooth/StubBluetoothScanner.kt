package com.cyclopen.trainer.bluetooth

import com.cyclopen.trainer.model.BtDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Stub implementation for Linux - Bluetooth not supported yet.
 */
class StubBluetoothScanner : BluetoothScanner {
    override fun scanDevices(): Flow<BtDevice> = flowOf()
    
    override fun stopScan() {
        // No-op
    }
    
    override fun isSupported(): Boolean = false
}
