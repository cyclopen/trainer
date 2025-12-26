package com.cyclopen.trainer.di

import com.cyclopen.trainer.bluetooth.BluetoothProvider
import org.koin.dsl.module

/**
 * Linux-specific Koin module (stub).
 */
val linuxModule = module {
    single { BluetoothProvider() }
}
