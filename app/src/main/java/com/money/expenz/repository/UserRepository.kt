package com.money.expenz.repository

import androidx.annotation.WorkerThread
import com.money.expenz.data.User
import com.money.expenz.model.UserDAO
import kotlinx.coroutines.flow.Flow

/** UserRepo to perform database operations which can be called via ViewModel
 * Coroutine used to avoid database operations on main thread
 */
class UserRepository(private val userDAO: UserDAO) {

    val userData: Flow<List<User>> = userDAO.getUserDetails()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertUserData(user: User) {
        userDAO.insertUser(user)
    }
}
