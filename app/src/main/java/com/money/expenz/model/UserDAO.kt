package com.money.expenz.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.money.expenz.data.IEDetails
import com.money.expenz.data.User
import com.money.expenz.data.UserWithIEDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIEDetails(ieDetails: IEDetails)

    @Query("SELECT * FROM User")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE id LIKE :userId")
    suspend fun getLoggedInUserDetails(userId: Int): User

    @Transaction
    @Query("SELECT * FROM User")
    fun getUserWithIEDetails(): Flow<List<UserWithIEDetails>>

    @Update
    suspend fun updateUserDetails(user: User)

    @Delete
    suspend fun deleteUserIEDetails(employee: IEDetails)
}
