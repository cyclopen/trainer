package com.cyclopen.trainer.di

import com.cyclopen.trainer.bluetooth.BluetoothProvider
import com.cyclopen.trainer.ui.DevicesViewModel
import com.cyclopen.trainer.ui.WorkoutViewModel
import org.koin.dsl.module

/**
 * Common Koin module.
 * Platform-specific modules should provide BluetoothProvider.
 */
val commonModule = module {
    single { DevicesViewModel(get()) }
    single { WorkoutViewModel(get()) }
}
