package com.money.expenz.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.money.expenz.data.User
import com.money.expenz.database.UserDatabase
import com.money.expenz.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * ExpenzViewModel for communication with database, DAO , Repositories
 * to provide livedata objects to handle UI events
 */
class ExpenzViewModel(application: Application) : AndroidViewModel(application) {

    private var _isUserLoggedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val isUserLoggedIn: MutableStateFlow<Boolean> get() = _isUserLoggedIn

    private var users: List<User>

    private var repository: UserRepository

    init {
        val userDb = UserDatabase.getInstance(application)
        val userDAO = userDb.userDAO()
        repository = UserRepository(userDAO)
        users = repository.userData
    }

    fun getUserDetails(): List<User> {
        return repository.userData
    }

    fun checkUserLoggedIn(userName: String, password: String): Boolean {
        getUserDetails().forEach { user ->
            _isUserLoggedIn.value =
                user.userName == userName && user.password == password
            Log.d("sailee", "Username " + user.userName)
            Log.d("sailee", "password " + user.password)
        }
        return _isUserLoggedIn.value
    }

    fun registerUser(registerUser: User) {
        repository.insertUserData(registerUser)
        _isUserLoggedIn.value = true
    }
}