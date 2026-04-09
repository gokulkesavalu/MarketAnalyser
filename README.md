# Market Analyser 📈

[![CI](https://github.com/your-username/MarketAnalyser/actions/workflows/ci.yml/badge.svg)](https://github.com/your-username/MarketAnalyser/actions/workflows/ci.yml)
[![Release](https://github.com/your-username/MarketAnalyser/actions/workflows/release.yml/badge.svg)](https://github.com/your-username/MarketAnalyser/actions/workflows/release.yml)

Market Analyser is a modern Android application built to demonstrate **Clean Architecture**, **MVVM**, and the latest **Jetpack Compose** practices. It fetches real-time currency exchange rates using the [Alpha Vantage API](https://www.alphavantage.co/).

---

## 🚀 Features

- **Real-time Exchange Rates** — Fetch live rates for any currency pair (e.g. USD → JPY)
- **Home Screen Navigation** — Dedicated landing screen with navigation to the exchange rate feature
- **Modern UI** — Fully built with Jetpack Compose and Material 3, including light/dark theme support
- **Reactive State** — `StateFlow` + `collectAsStateWithLifecycle()` for lifecycle-safe UI state
- **Dependency Injection** — Hilt for compile-time verified, testable dependency graphs
- **Networking** — Retrofit + OkHttp with an API key interceptor (no per-call key passing)
- **Compose Stability** — `@Immutable` on UI state models to prevent unnecessary recompositions
- **StrictMode** — Enabled in debug builds to catch UI-thread violations early
- **R8 Minification** — Enabled for release builds with resource shrinking and ProGuard rules
- **CI/CD** — GitHub Actions pipeline for automated testing and release publishing

---

## 🛠 Tech Stack

| Category | Library / Tool |
|---|---|
| Language | [Kotlin](https://kotlinlang.org/) |
| UI | [Jetpack Compose](https://developer.android.com/jetpack/compose) + Material 3 |
| Navigation | [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) |
| Dependency Injection | [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) |
| Networking | [Retrofit](https://square.github.io/retrofit/) + [OkHttp](https://square.github.io/okhttp/) |
| JSON Parsing | [Gson](https://github.com/google/gson) |
| Async | [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) + [Flow](https://kotlinlang.org/docs/flow.html) |
| Testing | JUnit 4, [MockK](https://mockk.io/), kotlinx-coroutines-test |
| Build | AGP 8.2.2, Gradle 8.6, JDK 17 |

---

## 🏗 Architecture

The project follows **Clean Architecture** with strict layer separation:

```
app/
├── data/
│   ├── api/                   # Retrofit interface (MarketApi)
│   ├── model/                 # DTOs (ExchangeRateDto, ExchangeRateResponse)
│   └── repository/            # ExchangeRateRepositoryImpl
├── domain/
│   ├── model/                 # Domain model (ExchangeRate) — @Immutable
│   └── repository/            # ExchangeRateRepository interface
├── ui/
│   ├── feature/
│   │   ├── home/              # HomeScreen (stateless composable)
│   │   └── exchangerates/     # ExchangeRateScreen + ExchangeRateViewModel
│   ├── navigation/            # AppNavGraph, Screen sealed class
│   └── theme/                 # MarketAnalyserTheme
└── di/
    └── AppModule              # Hilt module — OkHttpClient, Retrofit, Repository binding
```

### Key design decisions

- **Stateless screens** — All composables take `uiState` + callbacks. ViewModels live in the navigation graph (`AppNavGraph`), not in the screen composable, keeping previews dependency-free.
- **`@Binds` over `@Provides`** — Repository binding uses `@Binds` for zero-overhead Hilt wiring.
- **API key via OkHttp interceptor** — The key is added to every request automatically; no parameter threading through the call stack.
- **Route strings eliminated** — `Screen.route` is derived from `this::class.simpleName` — no hardcoded strings.
- **`remember`-wrapped lambdas** — Callbacks passed to screens are memoised with `remember(viewModel)` to prevent spurious recompositions.

---

## ⚙️ Setup & Installation

1. **Clone the project:**
   ```bash
   git clone https://github.com/your-username/MarketAnalyser.git
   ```

2. **API Key:**
   - Sign up for a free key at [Alpha Vantage](https://www.alphavantage.co/support/#api-key).
   - Open `app/build.gradle.kts` and replace the placeholder:
     ```kotlin
     buildConfigField("String", "ALPHA_VANTAGE_API_KEY", "\"YOUR_API_KEY_HERE\"")
     ```

3. **JDK:**
   - The project requires **JDK 17**. Ensure your Android Studio or system JDK is set to 17.

4. **Build & Run:** Open in **Android Studio** and run on an emulator or physical device.

---

## 🧪 Testing

Unit tests cover the data and presentation layers:

| Test class | What it covers |
|---|---|
| `ExchangeRateRepositoryImplTest` | DTO → domain mapping, success/failure paths, field correctness |
| `ExchangeRateViewModelTest` | State transitions, validation, loading/error/success states |

Run all tests:
```bash
./gradlew test
```

---

## 🔒 Release Build

R8 minification and resource shrinking are enabled for release:
- `isMinifyEnabled = true`
- `isShrinkResources = true`
- ProGuard rules in `app/proguard-rules.pro` preserve Retrofit interfaces, Gson DTOs, OkHttp internals, and Hilt ViewModels.

---

## 🔄 CI / CD

Two GitHub Actions workflows are included under `.github/workflows/`.

### `ci.yml` — Continuous Integration

Triggered on every **push to `main`** and every **pull request targeting `main`**.

| Step | Action |
|---|---|
| Checkout | `actions/checkout@v4` |
| JDK 17 setup | `actions/setup-java@v4` (Temurin) |
| Unit tests | `./gradlew test` |
| Upload results | Test HTML reports retained for 7 days |

### `release.yml` — Release Pipeline

Triggered by a **`v*` tag push** (e.g. `v1.2.0`) or manually via **workflow dispatch**.

```
git push tag v1.0.0
        │
        ▼
┌──────────────┐     ┌───────────────────────┐     ┌──────────────────────┐
│  Stage 1     │────▶│  Stage 2              │────▶│  Stage 3             │
│  Unit Tests  │     │  Build signed AAB     │     │  GitHub Release      │
│  (must pass) │     │  bundleRelease + R8   │     │  Auto release notes  │
└──────────────┘     │  Upload artifact      │     │  Attaches .aab       │
                     └───────────────────────┘     └──────────────────────┘
```


#### Cutting a release

```bash
git tag v1.0.0
git push origin v1.0.0
```

The pipeline runs tests → builds a signed AAB → publishes a GitHub Release with auto-generated release notes and the AAB attached.

---

## 📡 API Reference

**Alpha Vantage — Currency Exchange Rate**

```
GET https://www.alphavantage.co/query
  ?function=CURRENCY_EXCHANGE_RATE
  &from_currency=USD
  &to_currency=JPY
  &apikey=YOUR_KEY
```

Sample response field mapping:

| JSON key | DTO field | Domain field |
|---|---|---|
| `5. Exchange Rate` | `exchangeRate: String` | `exchangeRate: Double` |
| `8. Bid Price` | `bidPrice: String` | `bidPrice: Double` |
| `9. Ask Price` | `askPrice: String` | `askPrice: Double` |
