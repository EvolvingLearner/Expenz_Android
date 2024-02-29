package com.money.expenz.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.money.expenz.data.User
import com.money.expenz.model.UserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [User::class], version = 1, exportSchema = true)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDAO(): UserDAO

    private class UserDatabaseCallBack(private val coroutineScope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                    coroutineScope.launch {
                        populateDatabase(database.userDAO())
                    }
                }
            }

            suspend fun populateDatabase(userDao: UserDAO) {
                // Delete all content here.
                // Add sample words.
                var user = User(userName = "Sam")
                userDao.insertUser(user)
                user = User(password = "World!")
                userDao.insertUser(user)
            }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context, coroutineScope: CoroutineScope): UserDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "user_database"
                    ).addCallback(UserDatabaseCallBack(coroutineScope)).build()
                    INSTANCE = instance
                    instance
                }
            }
    }
}