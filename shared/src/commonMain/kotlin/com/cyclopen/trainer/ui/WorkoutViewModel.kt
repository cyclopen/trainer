package com.cyclopen.trainer.ui

import com.cyclopen.trainer.bluetooth.BluetoothProvider
import com.cyclopen.trainer.model.Telemetry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the workout screen.
 */
class WorkoutViewModel(private val bluetoothProvider: BluetoothProvider) {
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private val connection = bluetoothProvider.createConnection()
    
    private val _telemetry = MutableStateFlow(Telemetry())
    val telemetry: StateFlow<Telemetry> = _telemetry.asStateFlow()
    
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun connectToDevice(deviceAddress: String) {
        scope.launch {
            val result = connection.connect(deviceAddress)
            result.onSuccess {
                _isConnected.value = true
                _error.value = null
                
                // Start observing telemetry
                connection.observeTelemetry().collect { telemetry ->
                    _telemetry.value = telemetry
                }
            }.onFailure { e ->
                _isConnected.value = false
                _error.value = e.message
            }
        }
    }
    
    fun disconnect() {
        scope.launch {
            connection.disconnect()
            _isConnected.value = false
            _telemetry.value = Telemetry()
        }
    }
}
