package com.money.expenz.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.money.expenz.R
import com.money.expenz.data.DataSource
import com.money.expenz.data.Subscription
import com.money.expenz.database.UserDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class DataSourceTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var localDataSource: DataSource
    private lateinit var database: UserDatabase

    @Before
    fun setup() {
        // Using an in-memory database for testing, because it doesn't survive killing the process.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java
        )
            .allowMainThreadQueries() // allows queries on the main thread (Never use for production)
            .build()

        localDataSource = DataSource()
    }

    @Test
    fun loadSubscriptions() = runBlocking {
        val list = listOf(
            Subscription(R.string.netflix, R.drawable.ic_category_media),
            Subscription(R.string.wifi, R.drawable.ic_category_wifi),
            Subscription(R.string.amazon, R.drawable.ic_category_media),
            Subscription(R.string.vpn, R.drawable.ic_category_vpn),
            Subscription(R.string.electricity, R.drawable.ic_category_electricity),
            Subscription(R.string.mobile, R.drawable.ic_category_mobile),
        )

        val result = localDataSource.loadSubscriptions()
        assertEquals(result, list)
    }

    @After
    fun cleanUp() {
        database.close()
    }
}
