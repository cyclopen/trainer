package com.cyclopen.trainer.bluetooth

/**
 * Standard Bluetooth GATT service and characteristic UUIDs for fitness devices.
 */
object BleUuids {
    // Heart Rate Service
    const val HEART_RATE_SERVICE = "0000180d-0000-1000-8000-00805f9b34fb"
    const val HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb"
    
    // Cycling Speed and Cadence Service
    const val CSC_SERVICE = "00001816-0000-1000-8000-00805f9b34fb"
    const val CSC_MEASUREMENT = "00002a5b-0000-1000-8000-00805f9b34fb"
    
    // Cycling Power Service
    const val CYCLING_POWER_SERVICE = "00001818-0000-1000-8000-00805f9b34fb"
    const val CYCLING_POWER_MEASUREMENT = "00002a63-0000-1000-8000-00805f9b34fb"
    const val CYCLING_POWER_CONTROL_POINT = "00002a66-0000-1000-8000-00805f9b34fb"
    
    // Fitness Machine Service (FTMS)
    const val FTMS_SERVICE = "00001826-0000-1000-8000-00805f9b34fb"
    const val FTMS_INDOOR_BIKE_DATA = "00002ad2-0000-1000-8000-00805f9b34fb"
    const val FTMS_CONTROL_POINT = "00002ad9-0000-1000-8000-00805f9b34fb"
    
    // Device Information Service
    const val DEVICE_INFORMATION_SERVICE = "0000180a-0000-1000-8000-00805f9b34fb"
}
