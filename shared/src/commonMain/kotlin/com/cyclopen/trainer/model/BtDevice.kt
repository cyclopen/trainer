package com.cyclopen.trainer.model

import kotlinx.serialization.Serializable

/**
 * Represents a discovered Bluetooth device.
 */
@Serializable
data class BtDevice(
    val address: String,
    val name: String?,
    val rssi: Int,
    val type: DeviceType = DeviceType.UNKNOWN
)
