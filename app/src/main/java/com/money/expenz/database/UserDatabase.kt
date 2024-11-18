package com.money.expenz.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.money.expenz.data.IEDetails
import com.money.expenz.data.User
import com.money.expenz.model.UserDAO
import kotlinx.coroutines.CoroutineScope

@Database(entities = [User::class, IEDetails::class], version = 1, exportSchema = true)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(
            context: Context,
            coroutineScope: CoroutineScope,
        ): UserDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "user_database",
                    ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
