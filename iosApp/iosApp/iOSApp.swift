import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        // Initialize Koin DI
        KoinModuleKt.doInitKoin(appDeclaration: { _ in })
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
