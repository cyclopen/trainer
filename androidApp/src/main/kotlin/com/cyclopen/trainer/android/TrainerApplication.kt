package com.cyclopen.trainer.android

import android.app.Application
import com.cyclopen.trainer.di.androidModule
import com.cyclopen.trainer.di.commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TrainerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@TrainerApplication)
            modules(commonModule, androidModule(this@TrainerApplication))
        }
    }
}
