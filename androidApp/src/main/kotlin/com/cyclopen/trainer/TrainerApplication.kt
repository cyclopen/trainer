package com.cyclopen.trainer.android

import android.app.Application
import com.cyclopen.trainer.initKoinAndroid

/**
 * Android Application class
 */
class TrainerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin with Android context
        initKoinAndroid(this)
    }
}
