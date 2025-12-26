package com.cyclopen.trainer.android

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.cyclopen.trainer.ui.DevicesScreen
import com.cyclopen.trainer.ui.WorkoutScreen

/**
 * Main Android Activity with Compose setup
 */
class MainActivity : ComponentActivity() {
    
    private var hasPermissions by mutableStateOf(false)
    
    // Permission launcher
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasPermissions = permissions.values.all { it }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Request Bluetooth permissions
        requestBluetoothPermissions()
        
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrainerApp()
                }
            }
        }
    }
    
    private fun requestBluetoothPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        
        permissionLauncher.launch(permissions)
    }
}

@Composable
fun TrainerApp() {
    var currentScreen by remember { mutableStateOf("devices") }
    
    when (currentScreen) {
        "devices" -> {
            DevicesScreen(
                onDeviceConnected = {
                    currentScreen = "workout"
                }
            )
        }
        "workout" -> {
            WorkoutScreen(
                onDisconnect = {
                    currentScreen = "devices"
                }
            )
        }
    }
}
