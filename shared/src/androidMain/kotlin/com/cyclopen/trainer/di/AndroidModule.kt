package com.cyclopen.trainer.di

import android.content.Context
import com.cyclopen.trainer.bluetooth.BluetoothProvider
import org.koin.dsl.module

/**
 * Android-specific Koin module.
 */
fun androidModule(context: Context) = module {
    single { BluetoothProvider(context) }
}
