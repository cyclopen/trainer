package com.cyclopen.trainer.bluetooth

import com.cyclopen.trainer.model.BtDevice
import com.cyclopen.trainer.model.DeviceType
import com.juul.kable.Scanner
import com.juul.kable.logs.Logging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * macOS implementation of BluetoothScanner using Kable (CoreBluetooth).
 */
class MacosBluetoothScanner : BluetoothScanner {
    private var scanner: Scanner? = null
    
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
                    address = advertisement.identifier.toString(),
                    name = name,
                    rssi = advertisement.rssi,
                    type = type
                )
            }
            .catch { e ->
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
