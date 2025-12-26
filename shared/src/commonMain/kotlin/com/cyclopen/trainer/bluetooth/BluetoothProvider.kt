package com.cyclopen.trainer.bluetooth

/**
 * Expect/Actual factory for creating platform-specific Bluetooth implementations.
 */
expect class BluetoothProvider {
    fun createScanner(): BluetoothScanner
    fun createConnection(): BluetoothConnection
    fun isBluetoothSupported(): Boolean
}
