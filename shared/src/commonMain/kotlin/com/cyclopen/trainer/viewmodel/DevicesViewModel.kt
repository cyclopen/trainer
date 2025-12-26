package com.cyclopen.trainer.viewmodel

import com.cyclopen.trainer.bluetooth.BluetoothScanner
import com.cyclopen.trainer.bluetooth.BluetoothConnection
import com.cyclopen.trainer.model.BtDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel for device scanning and pairing screen
 */
class DevicesViewModel(
    private val scanner: BluetoothScanner,
    private val connection: BluetoothConnection
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main + Job())
    
    private val _devices = MutableStateFlow<List<BtDevice>>(emptyList())
    val devices: StateFlow<List<BtDevice>> = _devices
    
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun startScanning() {
        if (_isScanning.value) return
        
        viewModelScope.launch {
            _isScanning.value = true
            _devices.value = emptyList()
            _error.value = null
            
            scanner.startScan()
                .catch { e ->
                    _error.value = "Scan failed: ${e.message}"
                    _isScanning.value = false
                }
                .collect { device ->
                    // Add or update device in list
                    val currentList = _devices.value.toMutableList()
                    val index = currentList.indexOfFirst { it.id == device.id }
                    if (index >= 0) {
                        currentList[index] = device
                    } else {
                        currentList.add(device)
                    }
                    _devices.value = currentList
                }
        }
    }
    
    fun stopScanning() {
        scanner.stopScan()
        _isScanning.value = false
    }
    
    fun connectToDevice(device: BtDevice) {
        viewModelScope.launch {
            try {
                val success = connection.connect(device)
                if (success) {
                    // Update device as connected
                    val updatedList = _devices.value.map {
                        if (it.id == device.id) it.copy(isConnected = true) else it
                    }
                    _devices.value = updatedList
                } else {
                    _error.value = "Failed to connect to ${device.name}"
                }
            } catch (e: Exception) {
                _error.value = "Connection error: ${e.message}"
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun onCleared() {
        stopScanning()
        // Cancel coroutines
    }
}
