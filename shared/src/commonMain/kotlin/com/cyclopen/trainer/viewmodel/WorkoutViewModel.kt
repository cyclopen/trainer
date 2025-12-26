package com.cyclopen.trainer.viewmodel

import com.cyclopen.trainer.bluetooth.BluetoothConnection
import com.cyclopen.trainer.model.Telemetry
import com.cyclopen.trainer.model.TrainerControl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel for workout/telemetry screen
 */
class WorkoutViewModel(
    private val connection: BluetoothConnection
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main + Job())
    
    private val _telemetry = MutableStateFlow(Telemetry())
    val telemetry: StateFlow<Telemetry> = _telemetry
    
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    init {
        startObservingTelemetry()
    }
    
    private fun startObservingTelemetry() {
        viewModelScope.launch {
            connection.observeTelemetry()
                .catch { e ->
                    _error.value = "Telemetry error: ${e.message}"
                }
                .collect { data ->
                    _telemetry.value = data
                    _isConnected.value = connection.isConnected()
                }
        }
    }
    
    fun setTargetPower(watts: Int) {
        viewModelScope.launch {
            try {
                val control = TrainerControl(targetPower = watts)
                val success = connection.sendControl(control)
                if (!success) {
                    _error.value = "Failed to set target power"
                }
            } catch (e: Exception) {
                _error.value = "Control error: ${e.message}"
            }
        }
    }
    
    fun setResistance(level: Int) {
        viewModelScope.launch {
            try {
                val control = TrainerControl(resistance = level)
                val success = connection.sendControl(control)
                if (!success) {
                    _error.value = "Failed to set resistance"
                }
            } catch (e: Exception) {
                _error.value = "Control error: ${e.message}"
            }
        }
    }
    
    fun disconnect() {
        viewModelScope.launch {
            connection.disconnect()
            _isConnected.value = false
        }
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun onCleared() {
        disconnect()
        // Cancel coroutines
    }
}
