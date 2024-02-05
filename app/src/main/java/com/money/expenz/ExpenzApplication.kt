package com.money.expenz

import android.app.Application
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
}