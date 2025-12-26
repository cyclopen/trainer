package com.cyclopen.trainer.di

import com.cyclopen.trainer.bluetooth.BluetoothProvider
import org.koin.dsl.module

/**
 * Windows-specific Koin module (stub).
 */
val mingwModule = module {
    single { BluetoothProvider() }
}
