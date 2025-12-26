# Trainer - Kotlin Multiplatform Fitness App

A Kotlin Multiplatform (KMP) Compose application for connecting to Bluetooth fitness devices (heart rate monitors, smart trainers, power meters, etc.) with support for Android, iOS, macOS, Windows, and Linux.

## Overview

This project demonstrates a complete KMP architecture using:
- **Kotlin 2.0.21** for multiplatform development
- **Compose Multiplatform 1.7.0** for shared UI across platforms
- **Kable 0.34.0** for Bluetooth Low Energy (BLE) communication
- **Koin 3.5.x** for dependency injection
- **Coroutines 1.9.x** for async operations
- **Kotlinx Serialization 1.7.x** for data handling

## Supported Platforms & Bluetooth Support

| Platform | Target | Bluetooth Support | Status |
|----------|--------|-------------------|--------|
| Android | `android` | ✅ Kable (via Android BLE APIs) | Fully supported |
| iOS | `iosArm64`, `iosSimulatorArm64` | ✅ Kable (via CoreBluetooth) | Fully supported |
| macOS | `macosArm64`, `macosX64` | ✅ Kable (via CoreBluetooth) | Fully supported |
| Windows | `mingwX64` | ⚠️ Stub implementation | Compiles but BLE inactive |
| Linux | `linuxX64` | ⚠️ Stub implementation | Compiles but BLE inactive |

**Note**: Windows and Linux targets use stub implementations that compile successfully but return "unsupported" for Bluetooth operations. These can be extended in the future with platform-specific BLE libraries.

## Architecture

### Modules

- **`:shared`**: Common code and UI
  - Common Kotlin code with expect/actual for platform-specific Bluetooth
  - Shared Compose UI (Devices and Workout screens)
  - ViewModels with StateFlow for reactive state management
  - Common models: `DeviceType`, `BtDevice`, `Telemetry`, `TrainerControl`
  
- **`:androidApp`**: Android application wrapper
  - MainActivity that hosts the Compose UI
  - Android-specific permissions handling

- **`iosApp`**: iOS application (Xcode project)
  - SwiftUI wrapper for the shared Compose UI
  - iOS-specific entitlements

### Bluetooth Implementation

#### Expect/Actual Pattern

The project uses Kotlin's expect/actual mechanism for platform-specific Bluetooth:

```kotlin
// Common (expect)
expect class BluetoothProvider {
    fun createScanner(): BluetoothScanner
    fun createConnection(): BluetoothConnection
    fun isBluetoothSupported(): Boolean
}
```

#### Platform Implementations

**Android** (`androidMain`):
- Uses Kable with Android BLE APIs
- Requires runtime permissions (BLUETOOTH_SCAN, BLUETOOTH_CONNECT, ACCESS_FINE_LOCATION)
- Implementation: `AndroidBluetoothScanner`, `AndroidBluetoothConnection`

**iOS/macOS** (`iosMain`, `macosMain`):
- Uses Kable with CoreBluetooth
- Requires Info.plist entitlements for Bluetooth usage
- Implementation: `IosBluetoothScanner`, `IosBluetoothConnection`, `MacosBluetoothScanner`, `MacosBluetoothConnection`

**Windows/Linux** (`mingwMain`, `linuxMain`):
- Stub implementations that return empty flows and "unsupported" errors
- Can be extended with platform-specific BLE libraries in the future

### Bluetooth Device Types & Standards

The app recognizes standard Bluetooth GATT services:

- **Heart Rate**: Service `0x180D`, Characteristic `0x2A37`
- **Cycling Speed & Cadence (CSC)**: Service `0x1816`, Characteristic `0x2A5B`
- **Cycling Power**: Service `0x1818`, Characteristics `0x2A63` (measurement), `0x2A66` (control)
- **Fitness Machine (FTMS)**: Service `0x1826`, Characteristics `0x2AD2` (indoor bike data), `0x2AD9` (control point)

**TODO**: Full characteristic parsing and control point commands are placeholders. Current implementation provides the structure for:
- Subscribing to notifications
- Parsing telemetry (HR, speed, cadence, power)
- Sending control commands to trainers

## Project Structure

```
trainer/
├── shared/
│   ├── src/
│   │   ├── commonMain/kotlin/com/cyclopen/trainer/
│   │   │   ├── model/           # Data models
│   │   │   ├── bluetooth/       # BLE interfaces & UUIDs
│   │   │   ├── ui/              # Compose screens & ViewModels
│   │   │   └── di/              # Koin DI modules
│   │   ├── androidMain/kotlin/com/cyclopen/trainer/
│   │   │   ├── bluetooth/       # Android Kable implementations
│   │   │   └── di/              # Android DI module
│   │   ├── iosMain/kotlin/com/cyclopen/trainer/
│   │   │   ├── bluetooth/       # iOS Kable implementations
│   │   │   └── di/              # iOS DI module
│   │   ├── macosMain/kotlin/com/cyclopen/trainer/
│   │   │   ├── bluetooth/       # macOS Kable implementations
│   │   │   └── di/              # macOS DI module
│   │   ├── mingwMain/kotlin/com/cyclopen/trainer/
│   │   │   ├── bluetooth/       # Windows stub implementations
│   │   │   └── di/              # Windows DI module
│   │   ├── linuxMain/kotlin/com/cyclopen/trainer/
│   │   │   ├── bluetooth/       # Linux stub implementations
│   │   │   └── di/              # Linux DI module
│   │   └── desktopMain/kotlin/com/cyclopen/trainer/
│   │       └── Main.kt          # Desktop entry point
│   └── build.gradle.kts
├── androidApp/
│   ├── src/main/
│   │   ├── kotlin/              # MainActivity, Application
│   │   ├── AndroidManifest.xml
│   │   └── res/
│   └── build.gradle.kts
├── iosApp/
│   └── iosApp/
│       ├── iOSApp.swift         # SwiftUI entry point
│       └── ContentView.swift    # Compose wrapper
├── settings.gradle.kts
├── build.gradle.kts
└── gradle.properties
```

## Getting Started

### Prerequisites

- JDK 11 or higher
- Android Studio (for Android development)
- Xcode (for iOS/macOS development)
- Gradle 8.7+ (included via wrapper)

### Building

#### Build All Targets

```bash
./gradlew build
```

#### Android

```bash
# Build Android app
./gradlew :androidApp:assembleDebug

# Install on device
./gradlew :androidApp:installDebug

# Or open in Android Studio and run
```

**Runtime Permissions**: The app will need runtime permissions for Bluetooth scanning and location access. Ensure you grant these when prompted.

#### iOS

1. Build the shared framework:
   ```bash
   ./gradlew :shared:linkDebugFrameworkIosArm64        # For device
   ./gradlew :shared:linkDebugFrameworkIosSimulatorArm64  # For simulator
   ```

2. Open `iosApp/iosApp.xcodeproj` in Xcode
3. Select your target device or simulator
4. Build and run (Cmd+R)

**Info.plist Entitlements**: Add Bluetooth usage descriptions:
```xml
<key>NSBluetoothAlwaysUsageDescription</key>
<string>This app needs Bluetooth to connect to fitness devices.</string>
```

#### macOS

```bash
# Build shared framework for macOS
./gradlew :shared:linkDebugFrameworkMacosArm64  # For Apple Silicon
./gradlew :shared:linkDebugFrameworkMacosX64    # For Intel

# Run desktop app (uses JVM target)
./gradlew :shared:run
```

Or create a standalone macOS app using Compose Desktop packaging.

#### Desktop (Windows/Linux)

```bash
# Run desktop app
./gradlew :shared:run
```

**Note**: Bluetooth functionality will not work on Windows/Linux with the current stub implementations.

## Usage

1. **Devices Screen**:
   - Tap "Start Scan" to search for nearby Bluetooth fitness devices
   - Discovered devices appear in a list with their type, signal strength, and address
   - On unsupported platforms, a warning message is displayed
   - Tap a device to connect

2. **Workout Screen**:
   - Shows connection status
   - Displays real-time telemetry: heart rate, speed, cadence, power, distance
   - "Disconnect" button returns to the devices screen

## Development

### Adding a New Platform

1. Create platform-specific source set in `shared/build.gradle.kts`
2. Implement `BluetoothProvider` actual in `src/<platform>Main/kotlin/`
3. Implement `BluetoothScanner` and `BluetoothConnection` interfaces
4. Create platform-specific DI module
5. Update the desktop `Main.kt` to include the new platform

### Extending Bluetooth Functionality

- **Add Service/Characteristic Parsing**: Implement parsers in the connection classes (see `parseHeartRate` example)
- **Add Control Commands**: Implement encoding in `sendControl` method
- **Support More Device Types**: Extend `BleUuids` and `inferDeviceType` logic

### Testing

Currently, the project focuses on manual testing with real BLE devices. To add automated tests:

- Unit tests for parsers and business logic in `commonTest`
- Platform-specific tests in `androidTest`, `iosTest`, etc.
- Use mock Bluetooth implementations for CI/CD

## Dependencies

- **Kotlin**: 2.0.21
- **Compose Multiplatform**: 1.7.0
- **Android Gradle Plugin**: 8.5.2
- **Kable**: 0.34.0 (Bluetooth)
- **Kotlinx Coroutines**: 1.9.0
- **Kotlinx Serialization**: 1.7.3
- **Koin**: 3.5.6 (DI)
- **AndroidX Activity Compose**: 1.9.2
- **AndroidX Lifecycle**: 2.8.5

## Known Limitations & TODOs

- **Characteristic Parsing**: Full parsing of HR, CSC, Power, and FTMS characteristics is TODO
- **Control Commands**: Encoding and sending trainer control commands is TODO
- **Windows/Linux BLE**: Stub implementations; requires platform-specific libraries
- **iOS Xcode Project**: Basic structure provided; needs full project configuration
- **Error Handling**: Enhanced error states and retry logic
- **Permissions**: Runtime permission requests need UI flows (especially Android)

## License

This project is for demonstration purposes. Adjust licensing as needed.

## Contributing

Contributions are welcome! Please submit issues or pull requests for:
- Full characteristic parsing implementations
- Windows/Linux BLE support
- Enhanced error handling and UI polish
- Additional device type support
