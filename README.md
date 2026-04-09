# Market Analyser 📈

Market Analyser is a modern Android application built to demonstrate **Clean Architecture**, **MVVM**, and the latest **Jetpack Compose** practices. It fetches real-time currency exchange rates using the Alpha Vantage API.

## 🚀 Features
- **Real-time Rates:** Get the latest exchange rates for various currency pairs (e.g., USD to GBP).
- **Modern UI:** Fully built with Jetpack Compose and Material 3 design system.
- **Robust Architecture:** Follows Clean Architecture principles with clearly separated layers (Data, Domain, and UI).
- **Reactive State:** Uses `StateFlow` for UI state management in ViewModels.
- **Dependency Injection:** Powered by Hilt for a scalable and testable codebase.
- **Networking:** Retrofit and OkHttp for efficient API communication.

## 🛠 Tech Stack
- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Networking:** [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/)
- **Architecture:** MVVM + Clean Architecture
- **Asynchronous Work:** [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
- **JSON Parsing:** [Gson](https://github.com/google/gson)
- **Testing:** JUnit, MockK

## 📦 Project Structure
- `data/`: Implementation of repositories, API services, and DTOs.
- `domain/`: Business logic, use cases, and repository interfaces.
- `ui/`: Compose screens, ViewModels, and UI state models.
- `di/`: Hilt modules for providing dependencies.

## ⚙️ Setup & Installation
1. **Clone the project:**
   ```bash
   git clone https://github.com/your-username/MarketAnalyser.git
   ```
2. **API Key:**
   - Sign up for a free API key at [Alpha Vantage](https://www.alphavantage.co/support/#api-key).
   - Open `app/build.gradle.kts` and replace the placeholder in `buildConfigField`:
     ```kotlin
     buildConfigField("String", "ALPHA_VANTAGE_API_KEY", "\"YOUR_API_KEY_HERE\"")
     ```
3. **Build & Run:** Open the project in **Android Studio Ladybug** and run it on an emulator or physical device.

## 🧪 Testing
The project includes unit tests for key components:
- `ExchangeRateRepositoryImplTest`: Validates data mapping and error handling.
- Run tests via Gradle: `./gradlew test`
