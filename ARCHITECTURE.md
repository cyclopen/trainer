# Architecture Diagram

## High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        User Interface Layer                      │
├──────────────────────────┬──────────────────────────────────────┤
│   Android App            │         iOS App                      │
│                          │                                      │
│  MainActivity            │    iOSApp.swift                      │
│  ├─ Compose Setup        │    ├─ SwiftUI Wrapper               │
│  ├─ Permissions          │    └─ Compose Integration           │
│  └─ Navigation           │                                      │
└──────────────────────────┴──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Shared KMP Module (Compose)                   │
├─────────────────────────────────────────────────────────────────┤
│  UI Layer (Compose Multiplatform)                               │
│  ├─ DevicesScreen                                               │
│  │  └─ Device list, scan control, connection                    │
│  └─ WorkoutScreen                                               │
│     └─ Telemetry display, trainer controls                      │
├─────────────────────────────────────────────────────────────────┤
│  ViewModel Layer (StateFlow + Coroutines)                       │
│  ├─ DevicesViewModel                                            │
│  │  └─ Scanning, device list management                         │
│  └─ WorkoutViewModel                                            │
│     └─ Telemetry observation, control commands                  │
├─────────────────────────────────────────────────────────────────┤
│  Business Logic (Common)                                        │
│  ├─ BluetoothScanner (interface)                               │
│  │  └─ startScan(), stopScan(), requestPermissions()           │
│  ├─ BluetoothConnection (interface)                            │
│  │  └─ connect(), disconnect(), observeTelemetry()             │
│  └─ Models                                                      │
│     ├─ BtDevice, DeviceType                                    │
│     ├─ Telemetry (power, HR, cadence, speed)                   │
│     └─ TrainerControl (commands)                               │
├─────────────────────────────────────────────────────────────────┤
│  Dependency Injection (Koin)                                    │
│  ├─ Common Module (ViewModels)                                 │
│  └─ Platform Modules (expect/actual)                           │
└─────────────────────────────────────────────────────────────────┘
            │                           │
            ▼                           ▼
┌────────────────────────┐    ┌────────────────────────┐
│  Android Platform      │    │   iOS Platform         │
├────────────────────────┤    ├────────────────────────┤
│  actual implementations│    │  actual implementations│
│                        │    │                        │
│  AndroidBluetoothScanner│   │ IOSBluetoothScanner   │
│  ├─ BLE Scanner        │    │ ├─ CBCentralManager   │
│  └─ ScanCallback       │    │ └─ Delegate           │
│                        │    │                        │
│  AndroidBluetoothConn. │    │ IOSBluetoothConn.     │
│  ├─ BluetoothGatt      │    │ ├─ CBPeripheral       │
│  ├─ GattCallback       │    │ └─ Delegate           │
│  └─ Characteristic     │    │                        │
│     parsing            │    │                        │
└────────────────────────┘    └────────────────────────┘
            │                           │
            ▼                           ▼
┌────────────────────────┐    ┌────────────────────────┐
│  Bluetooth LE Stack    │    │  CoreBluetooth        │
├────────────────────────┤    ├────────────────────────┤
│  - Scanning            │    │  - Scanning           │
│  - GATT connections    │    │  - Peripheral mgmt    │
│  - Characteristic I/O  │    │  - Characteristic I/O │
│  - Services:           │    │  - Services:          │
│    • Heart Rate        │    │    • Heart Rate       │
│    • Cycling Power     │    │    • Cycling Power    │
│    • FTMS              │    │    • FTMS             │
└────────────────────────┘    └────────────────────────┘
```

## Data Flow

### Device Scanning Flow
```
User Action (Scan Button)
    │
    ▼
DevicesViewModel.startScanning()
    │
    ▼
BluetoothScanner.startScan() [expect]
    │
    ├─ Android: BLE Scanner → ScanCallback
    └─ iOS: CBCentralManager → didDiscoverPeripheral
    │
    ▼
Flow<BtDevice>
    │
    ▼
StateFlow (devices list)
    │
    ▼
DevicesScreen (Compose UI update)
```

### Connection & Telemetry Flow
```
User Action (Connect Button)
    │
    ▼
DevicesViewModel.connectToDevice()
    │
    ▼
BluetoothConnection.connect() [expect]
    │
    ├─ Android: BluetoothGatt.connect()
    └─ iOS: CBCentralManager.connect()
    │
    ▼
Service Discovery & Characteristic Subscription
    │
    ├─ Android: onServicesDiscovered → setCharacteristicNotification
    └─ iOS: didDiscoverServices → discoverCharacteristics
    │
    ▼
Characteristic Value Updates
    │
    ├─ Android: onCharacteristicChanged
    └─ iOS: didUpdateValueForCharacteristic
    │
    ▼
Parse BLE Data → Telemetry object
    │
    ▼
Flow<Telemetry>
    │
    ▼
StateFlow (WorkoutViewModel)
    │
    ▼
WorkoutScreen (Compose UI update)
```

### Control Command Flow
```
User Input (Set Power/Resistance)
    │
    ▼
WorkoutViewModel.setTargetPower() / setResistance()
    │
    ▼
TrainerControl object
    │
    ▼
BluetoothConnection.sendControl() [expect]
    │
    ├─ Android: Write to GATT characteristic
    └─ iOS: Write to CBCharacteristic
    │
    ▼
FTMS Control Point characteristic
    │
    ▼
Smart Trainer executes command
```

## Key Design Patterns

1. **Expect/Actual**: Platform-specific implementations
2. **Repository Pattern**: Bluetooth interfaces abstract platform details
3. **MVVM**: ViewModels manage UI state
4. **Reactive Streams**: Flow/StateFlow for async data
5. **Dependency Injection**: Koin for loose coupling
6. **Compose Multiplatform**: Single UI codebase

## BLE Service UUIDs (Standard)

- **Heart Rate Service**: 0x180D
  - Heart Rate Measurement: 0x2A37
  
- **Cycling Power Service**: 0x1818
  - Cycling Power Measurement: 0x2A63
  - Cycling Power Feature: 0x2A65
  
- **Fitness Machine Service (FTMS)**: 0x1826
  - Fitness Machine Feature: 0x2ACC
  - Indoor Bike Data: 0x2AD2
  - Training Status: 0x2AD3
  - Fitness Machine Control Point: 0x2AD9

## Bluetooth Permissions

### Android
```xml
<!-- Android 12+ -->
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />

<!-- Android < 12 -->
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

### iOS
```xml
<key>NSBluetoothAlwaysUsageDescription</key>
<string>Connect to cycling trainers and sensors</string>
```
