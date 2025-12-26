package com.cyclopen.trainer.di

import com.cyclopen.trainer.bluetooth.BluetoothProvider
import org.koin.dsl.module

/**
 * iOS-specific Koin module.
 */
val iosModule = module {
    single { BluetoothProvider() }
}
