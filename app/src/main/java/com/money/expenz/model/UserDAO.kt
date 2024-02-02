package com.money.expenz.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.money.expenz.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Dao
interface UserDAO {

    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM User")
    fun getUserDetails(): List<User>
}