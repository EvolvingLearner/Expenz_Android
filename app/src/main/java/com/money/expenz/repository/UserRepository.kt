package com.money.expenz.repository

import com.money.expenz.data.User
import com.money.expenz.model.UserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** UserRepo to perform database operations which can be called via ViewModel
 * Coroutine used to avoid database operations on main thread
 */
class UserRepository(private val userDAO: UserDAO) {

    val userData: List<User> = userDAO.getUserDetails()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertUserData(user: User) {
        coroutineScope.launch(Dispatchers.IO) { userDAO.insertUser(user) }
    }


    /* fun getUserData(): MutableStateFlow<List<User>> = withContext(Dispatchers.IO) {
        userDAO.getUserDetails()
    }*/
}