package com.cyclopen.trainer

import com.cyclopen.trainer.bluetooth.BluetoothConnection
import com.cyclopen.trainer.bluetooth.BluetoothScanner
import com.cyclopen.trainer.bluetooth.provideBluetoothConnection
import com.cyclopen.trainer.bluetooth.provideBluetoothScanner
import com.cyclopen.trainer.viewmodel.DevicesViewModel
import com.cyclopen.trainer.viewmodel.WorkoutViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/**
 * Common Koin module with shared dependencies
 */
val commonModule = module {
    // ViewModels
    single { DevicesViewModel(get(), get()) }
    single { WorkoutViewModel(get()) }
}

/**
 * Platform-specific module that should be provided by each platform
 * This will include platform-specific implementations of:
 * - BluetoothScanner
 * - BluetoothConnection
 */
expect val platformModule: Module

/**
 * Initialize Koin DI
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(commonModule, platformModule)
    }
}
