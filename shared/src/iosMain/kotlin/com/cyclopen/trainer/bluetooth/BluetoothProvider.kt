package com.cyclopen.trainer.bluetooth

/**
 * iOS actual implementation of BluetoothProvider.
 */
actual class BluetoothProvider {
    actual fun createScanner(): BluetoothScanner {
        return IosBluetoothScanner()
    }
    
    actual fun createConnection(): BluetoothConnection {
        return IosBluetoothConnection()
    }
    
    actual fun isBluetoothSupported(): Boolean = true
}
