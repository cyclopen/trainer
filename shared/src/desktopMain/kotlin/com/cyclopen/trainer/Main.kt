package com.cyclopen.trainer

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cyclopen.trainer.di.commonModule
import com.cyclopen.trainer.ui.App
import org.koin.core.context.startKoin

fun main() {
    // Determine the platform module based on OS
    val platformModule = when {
        System.getProperty("os.name").contains("Mac", ignoreCase = true) -> {
            com.cyclopen.trainer.di.macosModule
        }
        System.getProperty("os.name").contains("Windows", ignoreCase = true) -> {
            com.cyclopen.trainer.di.mingwModule
        }
        else -> {
            com.cyclopen.trainer.di.linuxModule
        }
    }
    
    startKoin {
        modules(commonModule, platformModule)
    }
    
    application {
        Window(onCloseRequest = ::exitApplication, title = "Trainer") {
            App()
        }
    }
}
