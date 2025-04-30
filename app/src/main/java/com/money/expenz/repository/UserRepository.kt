package com.money.expenz.repository

import androidx.annotation.WorkerThread
import com.money.expenz.data.IEDetails
import com.money.expenz.data.Subscription
import com.money.expenz.data.User
import com.money.expenz.data.UserWithIEDetails
import com.money.expenz.model.UserDAO
import kotlinx.coroutines.flow.Flow

/** UserRepo to perform database operations which can be called via ViewModel
 * Coroutine used to avoid database operations on main thread
 */
class UserRepository(private val userDAO: UserDAO) {
    @WorkerThread
    fun getUsers(): Flow<List<User>>? {
        return userDAO.getAllUsers()
    }

    @WorkerThread
    fun getUserWithIEDetails(loggedInUserId: Int): UserWithIEDetails {
        return userDAO.getUserWithIEDetails(loggedInUserId)
    }

    @WorkerThread
    suspend fun getLoggedInUserDetails(loggedInUserId: Int): User {
        return userDAO.getLoggedInUserDetails(loggedInUserId)
    }

    @WorkerThread
    suspend fun insertUserData(user: User) : Long {
        return userDAO.insertUser(user)
    }

    @WorkerThread
    suspend fun insertIEDetails(ieDetails: IEDetails) {
        userDAO.insertIEDetails(ieDetails)
    }

    @WorkerThread
    suspend fun insertSubscription(subscription: Subscription) {
        userDAO.insertSubscription(subscription)
    }

    suspend fun updateUserDetails(user: User) {
        userDAO.updateUserDetails(user)
    }
    suspend fun updateIEDetails(ieDetails: IEDetails) {
        userDAO.updateIEDetails(ieDetails)
    }

    @WorkerThread
    fun getAllSubscriptions(): Flow<List<Subscription>> {
        return userDAO.getAllSubscriptions()
    }

    suspend fun deleteIE(ieDetails: IEDetails){
        userDAO.deleteUserIEDetails(ieDetails)
    }

    @WorkerThread
    suspend fun searchIECategory(category: String): List<IEDetails> {
        return userDAO.searchIECategory(category)
    }
}
