package com.cyclopen.trainer.bluetooth

/**
 * macOS actual implementation of BluetoothProvider.
 */
actual class BluetoothProvider {
    actual fun createScanner(): BluetoothScanner {
        return MacosBluetoothScanner()
    }
    
    actual fun createConnection(): BluetoothConnection {
        return MacosBluetoothConnection()
    }
    
    actual fun isBluetoothSupported(): Boolean = true
}
