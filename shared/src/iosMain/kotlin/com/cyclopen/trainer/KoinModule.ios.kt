package com.cyclopen.trainer

import com.cyclopen.trainer.bluetooth.BluetoothConnection
import com.cyclopen.trainer.bluetooth.BluetoothScanner
import com.cyclopen.trainer.bluetooth.provideBluetoothConnection
import com.cyclopen.trainer.bluetooth.provideBluetoothScanner
import org.koin.dsl.module

/**
 * iOS platform module with iOS-specific implementations
 */
actual val platformModule = module {
    single<BluetoothScanner> { provideBluetoothScanner() }
    single<BluetoothConnection> { provideBluetoothConnection() }
}
