package com.cyclopen.trainer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyclopen.trainer.viewmodel.WorkoutViewModel
import org.koin.compose.koinInject

/**
 * Screen for displaying workout telemetry and controls
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    viewModel: WorkoutViewModel = koinInject(),
    onDisconnect: () -> Unit = {}
) {
    val telemetry by viewModel.telemetry.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    val error by viewModel.error.collectAsState()
    
    var targetPower by remember { mutableStateOf("150") }
    var resistance by remember { mutableStateOf("50") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout") },
                actions = {
                    if (isConnected) {
                        TextButton(
                            onClick = {
                                viewModel.disconnect()
                                onDisconnect()
                            }
                        ) {
                            Text("Disconnect")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Connection status
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isConnected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    }
                )
            ) {
                Text(
                    text = if (isConnected) "Connected" else "Disconnected",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            // Error message
            error?.let { errorMessage ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = errorMessage,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("Dismiss")
                        }
                    }
                }
            }
            
            // Telemetry data
            Text(
                text = "Telemetry",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TelemetryCard(
                    label = "Power",
                    value = telemetry.power?.toString() ?: "--",
                    unit = "W",
                    modifier = Modifier.weight(1f)
                )
                TelemetryCard(
                    label = "Heart Rate",
                    value = telemetry.heartRate?.toString() ?: "--",
                    unit = "BPM",
                    modifier = Modifier.weight(1f)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TelemetryCard(
                    label = "Cadence",
                    value = telemetry.cadence?.toString() ?: "--",
                    unit = "RPM",
                    modifier = Modifier.weight(1f)
                )
                TelemetryCard(
                    label = "Speed",
                    value = telemetry.speed?.let { "%.1f".format(it) } ?: "--",
                    unit = "km/h",
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Controls
            Text(
                text = "Controls",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            // Target power control
            OutlinedTextField(
                value = targetPower,
                onValueChange = { targetPower = it },
                label = { Text("Target Power (W)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isConnected
            )
            Button(
                onClick = {
                    targetPower.toIntOrNull()?.let { watts ->
                        viewModel.setTargetPower(watts)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isConnected
            ) {
                Text("Set Target Power")
            }
            
            // Resistance control
            OutlinedTextField(
                value = resistance,
                onValueChange = { resistance = it },
                label = { Text("Resistance (0-100)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isConnected
            )
            Button(
                onClick = {
                    resistance.toIntOrNull()?.let { level ->
                        viewModel.setResistance(level)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isConnected
            ) {
                Text("Set Resistance")
            }
        }
    }
}

@Composable
private fun TelemetryCard(
    label: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
