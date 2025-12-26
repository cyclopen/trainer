package com.cyclopen.trainer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WorkoutScreen(
    viewModel: WorkoutViewModel,
    deviceAddress: String?,
    onBack: () -> Unit
) {
    val telemetry by viewModel.telemetry.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    val error by viewModel.error.collectAsState()
    
    LaunchedEffect(deviceAddress) {
        deviceAddress?.let {
            viewModel.connectToDevice(it)
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Workout",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Button(onClick = onBack) {
                Text("Back")
            }
        }
        
        if (error != null) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = "Error: $error",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isConnected) "Connected" else "Connecting...",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isConnected) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
        
        // Telemetry display
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Telemetry",
                    style = MaterialTheme.typography.titleLarge
                )
                
                telemetry.heartRate?.let {
                    TelemetryRow("Heart Rate", "$it bpm")
                }
                
                telemetry.speed?.let {
                    TelemetryRow("Speed", String.format("%.1f km/h", it))
                }
                
                telemetry.cadence?.let {
                    TelemetryRow("Cadence", "$it rpm")
                }
                
                telemetry.power?.let {
                    TelemetryRow("Power", "$it W")
                }
                
                telemetry.distance?.let {
                    TelemetryRow("Distance", String.format("%.2f km", it))
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { 
                viewModel.disconnect()
                onBack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Disconnect")
        }
    }
}

@Composable
fun TelemetryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
