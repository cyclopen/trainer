package com.cyclopen.trainer.model

import kotlinx.serialization.Serializable

/**
 * Types of Bluetooth devices supported by the app
 */
enum class DeviceType {
    TRAINER,        // Smart trainer (e.g., KICKR, NEO)
    HEART_RATE,     // Heart rate monitor
    CADENCE,        // Cadence sensor
    SPEED,          // Speed sensor
    POWER_METER,    // Power meter
    UNKNOWN         // Unknown device type
}

/**
 * Represents a discovered Bluetooth device
 */
@Serializable
data class BtDevice(
    val id: String,
    val name: String,
    val type: DeviceType,
    val rssi: Int = 0,  // Signal strength
    val isConnected: Boolean = false
)

/**
 * Real-time telemetry data from connected devices
 */
@Serializable
data class Telemetry(
    val power: Int? = null,          // Watts
    val heartRate: Int? = null,      // BPM
    val cadence: Int? = null,        // RPM
    val speed: Double? = null,       // km/h
    val distance: Double? = null,    // km
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Control commands for smart trainers
 */
@Serializable
data class TrainerControl(
    val targetPower: Int? = null,        // Target power in watts
    val resistance: Int? = null,         // Resistance level (0-100)
    val simulation: SimulationMode? = null
)

/**
 * Trainer simulation modes
 */
@Serializable
enum class SimulationMode {
    ERG,        // Constant power (ERG mode)
    RESISTANCE, // Constant resistance
    SLOPE       // Simulate slope
}
