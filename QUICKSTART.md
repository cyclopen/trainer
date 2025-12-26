# Quick Start Guide

## Prerequisites

Ensure you have the following installed:

1. **JDK 11 or higher**
   ```bash
   java -version
   ```

2. **Android Studio** (Arctic Fox or later)
   - Download from: https://developer.android.com/studio
   - Install Android SDK API 24-34
   - Install Android Build Tools 34.0.0

3. **Xcode 14+** (for iOS development on macOS)
   ```bash
   xcode-select --install
   ```

4. **CocoaPods** (for iOS dependencies)
   ```bash
   sudo gem install cocoapods
   ```

## Building the Project

### 1. Clone the Repository

```bash
git clone https://github.com/cyclopen/trainer.git
cd trainer
```

### 2. Build the Shared Module

This will compile the KMP module with Android and iOS targets:

```bash
./gradlew :shared:build
```

### 3. Build Android App

#### Command Line
```bash
# Debug build
./gradlew :androidApp:assembleDebug

# Release build
./gradlew :androidApp:assembleRelease
```

#### Android Studio
1. Open Android Studio
2. File → Open → Select `trainer` directory
3. Wait for Gradle sync
4. Select "androidApp" run configuration
5. Click Run (▶️)

### 4. Build iOS Framework

Generate the iOS framework from the shared module:

```bash
# For iOS Simulator (Apple Silicon Mac)
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64

# For iOS Device
./gradlew :shared:linkDebugFrameworkIosArm64
```

The framework will be generated at:
```
shared/build/bin/iosSimulatorArm64/debugFramework/shared.framework
```

### 5. iOS App Setup

#### Option A: Create Xcode Project (Recommended)

1. Open Xcode
2. Create new iOS App project:
   - Product Name: `iosApp`
   - Organization Identifier: `com.cyclopen`
   - Interface: SwiftUI
   - Language: Swift

3. Add the generated `shared.framework`:
   - Project Settings → General → Frameworks, Libraries, and Embedded Content
   - Click `+` → Add Other → Add Files
   - Navigate to `shared/build/bin/iosSimulatorArm64/debugFramework/shared.framework`
   - Set to "Embed & Sign"

4. Copy Swift files:
   - Copy `iosApp/iosApp/iOSApp.swift` to Xcode project
   - Copy `iosApp/iosApp/ContentView.swift` to Xcode project
   - Copy `iosApp/iosApp/Info.plist` entries to project Info.plist

5. Build and Run in Xcode

#### Option B: Use Existing Files

If an Xcode project file (`.xcodeproj`) is added to the repository:

```bash
cd iosApp
open iosApp.xcodeproj
```

Then build and run in Xcode.

## Running the Apps

### Android

#### On Emulator
1. Start Android emulator from Android Studio (AVD Manager)
2. Run: `./gradlew :androidApp:installDebug`
3. App will launch automatically

#### On Device
1. Enable USB debugging on Android device
2. Connect device via USB
3. Run: `./gradlew :androidApp:installDebug`

### iOS

#### On Simulator
1. In Xcode, select a simulator device (e.g., iPhone 15 Pro)
2. Click Run (⌘R)

#### On Device
1. Connect iOS device
2. In Xcode, select your device
3. Configure signing team in project settings
4. Click Run (⌘R)

## Testing Bluetooth

### Android
1. Grant Bluetooth permissions when prompted
2. Ensure Bluetooth is enabled
3. Place a Bluetooth device (trainer, HR monitor) in pairing mode
4. Tap "Scan" button in the app
5. Select device to connect

### iOS
1. Grant Bluetooth permissions when prompted
2. Ensure Bluetooth is enabled
3. Place a Bluetooth device in pairing mode
4. Tap scan functionality
5. Select device to connect

## Development Workflow

### Making Changes

1. **Edit common code** in `shared/src/commonMain`
2. **Edit Android-specific code** in `shared/src/androidMain` or `androidApp`
3. **Edit iOS-specific code** in `shared/src/iosMain` or `iosApp`
4. Rebuild the module/app you changed

### Hot Reload

- **Android**: Supports Compose hot reload in Android Studio
- **iOS**: Supports SwiftUI preview (for Swift code only)

### Debugging

#### Android
- Use Android Studio debugger
- View Logcat for logs
- Use Bluetooth HCI snoop log for BLE debugging

#### iOS
- Use Xcode debugger
- View Console for logs
- Use PacketLogger for BLE debugging

## Common Issues

### Issue: "Plugin not found" error
**Solution**: Ensure internet connection for Gradle to download plugins

### Issue: iOS framework not found
**Solution**: Run `./gradlew :shared:linkDebugFrameworkIosSimulatorArm64` first

### Issue: Bluetooth permissions denied
**Solution**: Uninstall app and reinstall to re-request permissions

### Issue: No devices found when scanning
**Solution**: 
- Check Bluetooth is enabled
- Check app has required permissions
- Ensure device is in pairing mode
- On Android < 12, location permission is required

## Next Steps

After building successfully:

1. Implement full BLE protocol parsing (see TODOs in code)
2. Add device type detection from service UUIDs
3. Parse characteristic data for telemetry
4. Implement FTMS control commands
5. Add error handling and retry logic
6. Implement device reconnection
7. Add workout session recording
8. Add settings screen
9. Add unit tests
10. Add UI tests

## Documentation

- `README.md` - Project overview and architecture
- `ARCHITECTURE.md` - Detailed architecture diagrams
- `IMPLEMENTATION_SUMMARY.md` - Complete feature list
- `BUILD_NOTES.md` - Environment-specific build notes

## Support

For issues or questions:
- Check inline code comments
- Review TODO comments in source files
- See Kotlin Multiplatform docs: https://kotlinlang.org/docs/multiplatform.html
- See Compose Multiplatform docs: https://www.jetbrains.com/lp/compose-multiplatform/
