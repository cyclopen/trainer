package com.cyclopen.trainer.di

import com.cyclopen.trainer.bluetooth.BluetoothProvider
import org.koin.dsl.module

/**
 * macOS-specific Koin module.
 */
val macosModule = module {
    single { BluetoothProvider() }
}
