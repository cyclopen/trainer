package com.cyclopen.trainer.bluetooth

import android.annotation.SuppressLint
import android.content.Context
import com.cyclopen.trainer.model.BtDevice
import com.cyclopen.trainer.model.DeviceType
import com.juul.kable.Filter
import com.juul.kable.Scanner
import com.juul.kable.logs.Logging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch

/**
 * Android implementation of BluetoothScanner using Kable.
 */
class AndroidBluetoothScanner(private val context: Context) : BluetoothScanner {
    private var scanner: Scanner? = null
    
    @SuppressLint("MissingPermission")
    override fun scanDevices(): Flow<BtDevice> {
        val newScanner = Scanner {
            logging {
                level = Logging.Level.Warnings
            }
        }
        scanner = newScanner
        
        return newScanner.advertisements
            .map { advertisement ->
                val name = advertisement.name ?: "Unknown"
                val type = inferDeviceType(name, advertisement.uuids)
                
                BtDevice(
                    address = advertisement.address,
                    name = name,
                    rssi = advertisement.rssi,
                    type = type
                )
            }
            .catch { e ->
                // Handle errors - could emit error state
                e.printStackTrace()
            }
    }
    
    override fun stopScan() {
        scanner?.stop()
        scanner = null
    }
    
    override fun isSupported(): Boolean = true
    
    private fun inferDeviceType(name: String, uuids: List<String>): DeviceType {
        val lowerName = name.lowercase()
        
        return when {
            uuids.contains(BleUuids.HEART_RATE_SERVICE) || 
                lowerName.contains("heart") || lowerName.contains("hr") -> DeviceType.HEART_RATE
            uuids.contains(BleUuids.CSC_SERVICE) || 
                lowerName.contains("cadence") || lowerName.contains("speed") -> DeviceType.SPEED_CADENCE
            uuids.contains(BleUuids.CYCLING_POWER_SERVICE) || 
                lowerName.contains("power") -> DeviceType.POWER_METER
            uuids.contains(BleUuids.FTMS_SERVICE) || 
                lowerName.contains("trainer") || lowerName.contains("kickr") -> DeviceType.SMART_TRAINER
            else -> DeviceType.UNKNOWN
        }
    }
}
