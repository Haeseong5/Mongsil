import SwiftUI
import ComposeApp

struct ContentView: View {
    @State private var isDark: Bool = false

    var body: some View {
        ComposeView(onDarkThemeChange: { isDark = $0 })
            .ignoresSafeArea(.all)
            .preferredColorScheme(isDark ? .dark : .light)
    }
}

struct ComposeView: UIViewControllerRepresentable {
    let onDarkThemeChange: (Bool) -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        return MainViewControllerKt.MainViewController(onDarkThemeChange: { isDark in
            onDarkThemeChange(isDark.boolValue)
        })
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
