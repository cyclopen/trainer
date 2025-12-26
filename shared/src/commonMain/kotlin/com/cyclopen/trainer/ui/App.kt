package com.cyclopen.trainer.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import org.koin.compose.koinInject

@Composable
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Devices) }
        var selectedDeviceAddress by remember { mutableStateOf<String?>(null) }
        
        val devicesViewModel: DevicesViewModel = koinInject()
        val workoutViewModel: WorkoutViewModel = koinInject()
        
        when (val screen = currentScreen) {
            is Screen.Devices -> {
                DevicesScreen(
                    viewModel = devicesViewModel,
                    onDeviceSelected = { address ->
                        selectedDeviceAddress = address
                        currentScreen = Screen.Workout
                    }
                )
            }
            is Screen.Workout -> {
                WorkoutScreen(
                    viewModel = workoutViewModel,
                    deviceAddress = selectedDeviceAddress,
                    onBack = {
                        currentScreen = Screen.Devices
                        selectedDeviceAddress = null
                    }
                )
            }
        }
    }
}

sealed class Screen {
    data object Devices : Screen()
    data object Workout : Screen()
}
