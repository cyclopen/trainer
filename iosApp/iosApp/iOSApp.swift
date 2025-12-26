import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        // Initialize Koin
        KoinHelperKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
