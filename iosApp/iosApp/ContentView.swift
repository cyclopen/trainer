import SwiftUI
import shared

struct ContentView: View {
    @State private var showWorkout = false
    
    var body: some View {
        NavigationView {
            if showWorkout {
                ComposeWorkoutView()
            } else {
                ComposeDevicesView(onDeviceConnected: {
                    showWorkout = true
                })
            }
        }
    }
}

/**
 * SwiftUI wrapper for Compose Multiplatform DevicesScreen
 */
struct ComposeDevicesView: UIViewControllerRepresentable {
    let onDeviceConnected: () -> Void
    
    func makeUIViewController(context: Context) -> UIViewController {
        // TODO: Create UIViewController that hosts Compose DevicesScreen
        // This requires additional Compose UIKit integration
        let controller = UIViewController()
        controller.view.backgroundColor = .systemBackground
        
        let label = UILabel()
        label.text = "Devices Screen (Compose integration pending)"
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false
        
        controller.view.addSubview(label)
        NSLayoutConstraint.activate([
            label.centerXAnchor.constraint(equalTo: controller.view.centerXAnchor),
            label.centerYAnchor.constraint(equalTo: controller.view.centerYAnchor)
        ])
        
        return controller
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // Update if needed
    }
}

/**
 * SwiftUI wrapper for Compose Multiplatform WorkoutScreen
 */
struct ComposeWorkoutView: UIViewControllerRepresentable {
    
    func makeUIViewController(context: Context) -> UIViewController {
        // TODO: Create UIViewController that hosts Compose WorkoutScreen
        let controller = UIViewController()
        controller.view.backgroundColor = .systemBackground
        
        let label = UILabel()
        label.text = "Workout Screen (Compose integration pending)"
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false
        
        controller.view.addSubview(label)
        NSLayoutConstraint.activate([
            label.centerXAnchor.constraint(equalTo: controller.view.centerXAnchor),
            label.centerYAnchor.constraint(equalTo: controller.view.centerYAnchor)
        ])
        
        return controller
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // Update if needed
    }
}
