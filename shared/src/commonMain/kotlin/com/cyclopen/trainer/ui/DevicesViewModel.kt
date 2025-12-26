package com.cyclopen.trainer.ui

import com.cyclopen.trainer.bluetooth.BluetoothProvider
import com.cyclopen.trainer.model.BtDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the device scanning screen.
 */
class DevicesViewModel(private val bluetoothProvider: BluetoothProvider) {
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private val scanner = bluetoothProvider.createScanner()
    
    private val _devices = MutableStateFlow<List<BtDevice>>(emptyList())
    val devices: StateFlow<List<BtDevice>> = _devices.asStateFlow()
    
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()
    
    private val _isSupported = MutableStateFlow(bluetoothProvider.isBluetoothSupported())
    val isSupported: StateFlow<Boolean> = _isSupported.asStateFlow()
    
    fun startScan() {
        if (!bluetoothProvider.isBluetoothSupported()) {
            return
        }
        
        _isScanning.value = true
        _devices.value = emptyList()
        
        scope.launch {
            scanner.scanDevices().collect { device ->
                val currentDevices = _devices.value.toMutableList()
                val existingIndex = currentDevices.indexOfFirst { it.address == device.address }
                
                if (existingIndex >= 0) {
                    currentDevices[existingIndex] = device
                } else {
                    currentDevices.add(device)
                }
                
                _devices.value = currentDevices.sortedByDescending { it.rssi }
            }
        }
    }
    
    fun stopScan() {
        scanner.stopScan()
        _isScanning.value = false
    }
}
