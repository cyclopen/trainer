package com.cyclopen.trainer

import androidx.compose.ui.window.ComposeUIViewController
import com.cyclopen.trainer.di.commonModule
import com.cyclopen.trainer.di.iosModule
import com.cyclopen.trainer.ui.App
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    App()
}

fun initKoin() {
    startKoin {
        modules(commonModule, iosModule)
    }
}
