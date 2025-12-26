package com.cyclopen.trainer.bluetooth

import android.content.Context

/**
 * Android actual implementation of BluetoothProvider.
 */
actual class BluetoothProvider(private val context: Context) {
    actual fun createScanner(): BluetoothScanner {
        return AndroidBluetoothScanner(context)
    }
    
    actual fun createConnection(): BluetoothConnection {
        return AndroidBluetoothConnection(context)
    }
    
    actual fun isBluetoothSupported(): Boolean = true
}
