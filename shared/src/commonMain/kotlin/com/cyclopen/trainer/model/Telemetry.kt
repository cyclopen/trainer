package com.cyclopen.trainer.model

import kotlinx.serialization.Serializable

/**
 * Telemetry data from connected fitness devices.
 */
@Serializable
data class Telemetry(
    val heartRate: Int? = null,
    val speed: Float? = null,
    val cadence: Int? = null,
    val power: Int? = null,
    val distance: Float? = null,
    val timestamp: Long = System.currentTimeMillis()
)
