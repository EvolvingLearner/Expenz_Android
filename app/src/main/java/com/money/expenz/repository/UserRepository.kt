package com.money.expenz.repository

import androidx.annotation.WorkerThread
import com.money.expenz.data.IEDetails
import com.money.expenz.data.User
import com.money.expenz.data.UserWithIEDetails
import com.money.expenz.model.UserDAO
import kotlinx.coroutines.flow.Flow

/** UserRepo to perform database operations which can be called via ViewModel
 * Coroutine used to avoid database operations on main thread
 */
class UserRepository(private val userDAO: UserDAO) {

    val allUsersList: Flow<List<User>> = userDAO.getAllUsers()

    @WorkerThread
    fun getUserWithIEDetails(): Flow<List<UserWithIEDetails>> {
        return userDAO.getUserWithIEDetails()
    }

    @WorkerThread
    suspend fun getLoggedInUserDetails(loggedInUserId: Int): User {
        return userDAO.getLoggedInUserDetails(loggedInUserId)
    }

    @WorkerThread
    suspend fun insertUserData(user: User) {
        return userDAO.insertUser(user)
    }

    @WorkerThread
    suspend fun insertIEDetails(ieDetails: IEDetails) {
        userDAO.insertIEDetails(ieDetails)
    }

    suspend fun updateUserDetails(user: User) {
        userDAO.updateUserDetails(user)
    }
}
