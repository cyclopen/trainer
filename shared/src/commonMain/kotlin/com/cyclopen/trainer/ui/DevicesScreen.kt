package com.cyclopen.trainer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cyclopen.trainer.model.BtDevice
import com.cyclopen.trainer.model.DeviceType

@Composable
fun DevicesScreen(
    viewModel: DevicesViewModel,
    onDeviceSelected: (String) -> Unit
) {
    val devices by viewModel.devices.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()
    val isSupported by viewModel.isSupported.collectAsState()
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Bluetooth Devices",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (!isSupported) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = "Bluetooth is not supported on this platform",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.startScan() },
                enabled = !isScanning && isSupported,
                modifier = Modifier.weight(1f)
            ) {
                Text(if (isScanning) "Scanning..." else "Start Scan")
            }
            
            Button(
                onClick = { viewModel.stopScan() },
                enabled = isScanning,
                modifier = Modifier.weight(1f)
            ) {
                Text("Stop Scan")
            }
        }
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(devices) { device ->
                DeviceCard(device = device, onClick = { onDeviceSelected(device.address) })
            }
        }
    }
}

@Composable
fun DeviceCard(device: BtDevice, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = device.name ?: "Unknown Device",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Type: ${device.type.name}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "RSSI: ${device.rssi} dBm",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Address: ${device.address}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
