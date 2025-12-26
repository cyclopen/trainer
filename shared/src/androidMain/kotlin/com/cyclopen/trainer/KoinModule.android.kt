package com.cyclopen.trainer

import android.content.Context
import com.cyclopen.trainer.bluetooth.AndroidBluetoothConnection
import com.cyclopen.trainer.bluetooth.AndroidBluetoothScanner
import com.cyclopen.trainer.bluetooth.BluetoothConnection
import com.cyclopen.trainer.bluetooth.BluetoothScanner
import org.koin.dsl.module

/**
 * Android platform module with Android-specific implementations
 */
actual val platformModule = module {
    single<BluetoothScanner> { AndroidBluetoothScanner(get()) }
    single<BluetoothConnection> { AndroidBluetoothConnection(get()) }
}

/**
 * Helper to initialize Koin with Android context
 */
fun initKoinAndroid(context: Context) {
    initKoin {
        modules(
            module {
                single { context }
            }
        )
    }
}
