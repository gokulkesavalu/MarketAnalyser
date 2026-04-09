package co.uk.marketanalyser

import android.app.Application
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MarketAnalyserApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }
    }

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()              // disk reads/writes, network, custom slow calls
                .penaltyLog()            // log to Logcat with tag StrictMode
                .penaltyFlashScreen()    // brief red screen flash on violation
                .build()
        )

        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectAll()             // leaked objects, closeable not closed, cleartext traffic, etc.
                .penaltyLog()
                .build()
        )
    }
}
