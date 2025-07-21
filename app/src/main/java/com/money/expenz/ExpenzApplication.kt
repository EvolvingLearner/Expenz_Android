package com.money.expenz

import android.app.Application
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.amplitude.android.DefaultTrackingOptions
import com.amplitude.android.events.Identify
import com.money.expenz.database.UserDatabase
import com.money.expenz.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ExpenzApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { UserDatabase.getInstance(this, applicationScope) }
    val repository by lazy { UserRepository(database.userDAO()) }
    override fun onCreate() {
        super.onCreate()

        amplitudeInstance = Amplitude(
            Configuration(
                apiKey = "9ec2a63fe7417339c261a339e5c93cd5",
                context = this,
                defaultTracking = DefaultTrackingOptions.ALL
            )
        )

        val identify = Identify()
        identify.set("user-platform", "android")
        amplitudeInstance.identify(identify)
    }

    companion object {
        lateinit var amplitudeInstance: Amplitude
            private set
    }
}
