# Trainer - Kotlin Multiplatform Cycling App

A Kotlin Multiplatform (KMP) application with Compose Multiplatform for connecting to cycling trainers and sensors via Bluetooth. Targets Android and iOS platforms with shared business logic and UI.

## Architecture

### Project Structure

```
trainer/
├── shared/                          # KMP shared module
│   └── src/
│       ├── commonMain/              # Common Kotlin code
│       │   └── kotlin/
│       │       └── com/cyclopen/trainer/
│       │           ├── model/       # Data models (BtDevice, Telemetry, etc.)
│       │           ├── bluetooth/   # Bluetooth interfaces (expect declarations)
│       │           ├── viewmodel/   # ViewModels (DevicesViewModel, WorkoutViewModel)
│       │           ├── ui/          # Compose UI screens
│       │           └── KoinModule.kt # DI setup
│       ├── androidMain/             # Android-specific implementations
│       │   └── kotlin/
│       │       └── com/cyclopen/trainer/
│       │           └── bluetooth/   # Android BLE (Gatt) implementations
│       └── iosMain/                 # iOS-specific implementations
│           └── kotlin/
│               └── com/cyclopen/trainer/
│                   └── bluetooth/   # iOS CoreBluetooth implementations
├── androidApp/                      # Android app module
│   └── src/main/
│       ├── kotlin/                  # MainActivity, Application
│       └── AndroidManifest.xml      # Permissions and app config
└── iosApp/                          # iOS app module
    └── iosApp/
        ├── iOSApp.swift             # SwiftUI app entry
        ├── ContentView.swift        # Compose integration
        └── Info.plist               # iOS permissions
```

### Technology Stack

- **Kotlin**: 2.0.21
- **Compose Multiplatform**: 1.7.1
- **Gradle**: 8.7
- **Android Gradle Plugin**: 8.2.0
- **Coroutines**: 1.9.0
- **Serialization**: 1.7.3
- **Koin**: 3.5.6 (Dependency Injection)
- **Target Platforms**: Android (minSdk 24), iOS (iosArm64, iosSimulatorArm64)

### Expect/Actual Pattern

The project uses Kotlin's `expect`/`actual` mechanism for platform-specific implementations:

**Common (expect)**:
```kotlin
expect fun provideBluetoothScanner(): BluetoothScanner
expect fun provideBluetoothConnection(): BluetoothConnection
```

**Android (actual)**:
- Uses Android Bluetooth LE (GATT) APIs
- Implements `AndroidBluetoothScanner` and `AndroidBluetoothConnection`
- Handles runtime permissions (BLUETOOTH_SCAN, BLUETOOTH_CONNECT)

**iOS (actual)**:
- Uses CoreBluetooth framework (CBCentralManager, CBPeripheral)
- Implements `IOSBluetoothScanner` and `IOSBluetoothConnection`
- Permissions declared in Info.plist

### Data Models

- **DeviceType**: Enum for device types (TRAINER, HEART_RATE, CADENCE, etc.)
- **BtDevice**: Discovered Bluetooth device info
- **Telemetry**: Real-time workout data (power, heart rate, cadence, speed)
- **TrainerControl**: Commands for smart trainers (target power, resistance)

### Bluetooth Interfaces

- **BluetoothScanner**: Device discovery with Flow-based API
- **BluetoothConnection**: Device connection and data streaming

### ViewModels

- **DevicesViewModel**: Manages device scanning and connection
- **WorkoutViewModel**: Manages telemetry display and trainer controls

### UI Screens (Compose)

- **DevicesScreen**: Bluetooth device list with scan/connect functionality
- **WorkoutScreen**: Real-time telemetry display and trainer controls

### Dependency Injection

Koin is used for DI with platform-specific modules:
- Common module: ViewModels
- Platform modules: Bluetooth implementations (Android uses Context)

## Building & Running

### Prerequisites

- **JDK**: 11 or higher
- **Android Studio**: Arctic Fox or later
- **Xcode**: 14+ (for iOS)
- **Android SDK**: API 24-34
- **CocoaPods**: (for iOS dependencies)

### Android

1. **Build the project**:
   ```bash
   ./gradlew :androidApp:assembleDebug
   ```

2. **Run on device/emulator**:
   ```bash
   ./gradlew :androidApp:installDebug
   ```
   
   Or open in Android Studio and run the `androidApp` configuration.

3. **Permissions**: The app will request Bluetooth permissions at runtime.

### iOS

1. **Build the shared framework**:
   ```bash
   ./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
   # or for device:
   ./gradlew :shared:linkDebugFrameworkIosArm64
   ```

2. **Open in Xcode**:
   - Open `iosApp/iosApp.xcodeproj` (or create one if needed)
   - Link the generated `shared.framework` from `shared/build/bin/iosSimulatorArm64/debugFramework/`
   - Build and run on simulator or device

3. **Permissions**: Bluetooth permissions are declared in `Info.plist`

### Build All

```bash
./gradlew build
```

## Current Implementation Status

### ✅ Complete
- Project structure and Gradle configuration
- Data models and interfaces
- Expect/actual declarations for Bluetooth
- ViewModels with StateFlow
- Compose UI screens (DevicesScreen, WorkoutScreen)
- Koin DI setup
- Android manifest with permissions
- iOS Info.plist with permissions

### 🚧 TODO (Marked in Code)
- **Android**: Full BLE GATT implementation
  - Service UUID parsing for device type detection
  - Characteristic subscription and data parsing
  - FTMS (Fitness Machine Service) control implementation
  
- **iOS**: CoreBluetooth integration
  - Peripheral connection and service discovery
  - Characteristic parsing for telemetry
  - Control characteristic writes

- **iOS**: Complete Compose UIKit integration
  - UIViewController wrappers for Compose screens
  - SwiftUI <-> Compose interop

- **Both**: BLE protocol implementations
  - Heart Rate Service (0x180D)
  - Cycling Power Service (0x1818)
  - Fitness Machine Service (0x1826)
  - Device type detection from service UUIDs

## Development Notes

- **No Desktop Targets**: Project excludes desktop (JVM, macOS native) targets as specified
- **Bluetooth Stubs**: Current implementations are compile-ready skeletons with TODOs for full BLE stack
- **Testing**: No test infrastructure included (minimal change approach)
- **Assets**: No custom icons or assets included

## License

[Your License Here]

