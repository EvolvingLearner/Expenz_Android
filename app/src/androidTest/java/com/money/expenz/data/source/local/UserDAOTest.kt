package com.money.expenz.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.money.expenz.data.User
import com.money.expenz.database.UserDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class UserDAOTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: UserDatabase

    @Before
    fun initDatabase() {
        // Using an in-memory database
        // so that the information stored here disappears
        // when the process is killed.
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            UserDatabase::class.java
        ).build()
    }

    @Test
    fun insertUserAndGetDetails() = runBlockingTest {
        val user = User(id = 1, userName = "Ginger", email = "ginger@gmail.com", password = "test123", totalIncome = 200)
        database.userDAO().insertUser(user)

        val userInfo = database.userDAO().getUserDetails()
        assertThat(userInfo as Flow<List<*>>, notNullValue())
    }
    @After
    fun closeDb() = database.close()
}
