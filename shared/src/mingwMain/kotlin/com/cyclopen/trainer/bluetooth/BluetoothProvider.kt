package com.cyclopen.trainer.bluetooth

/**
 * Windows actual implementation of BluetoothProvider (stub).
 */
actual class BluetoothProvider {
    actual fun createScanner(): BluetoothScanner {
        return StubBluetoothScanner()
    }
    
    actual fun createConnection(): BluetoothConnection {
        return StubBluetoothConnection()
    }
    
    actual fun isBluetoothSupported(): Boolean = false
}
