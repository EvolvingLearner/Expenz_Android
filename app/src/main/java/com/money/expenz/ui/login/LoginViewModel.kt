package com.money.expenz.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.money.expenz.data.User
import com.money.expenz.repository.UserRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UserRepository,
) : ViewModel() {
    private var dbusers: MutableList<User> = mutableListOf()
    private val hasLoggedIn = MutableStateFlow(false)
    var loggedInUserId = 0

    val viewState =
        hasLoggedIn.map { hasLoggedIn ->
            if (hasLoggedIn) {
                ViewState.LoggedIn
            } else {
                ViewState.NotLoggedIn
            }
        }

    sealed class ViewState {
        object LoggedIn : ViewState() // hasLoggedIn = true

        object NotLoggedIn : ViewState() // hasLoggedIn = false
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("ExpenzViewModel", "Exception ${throwable.localizedMessage}")
    }

    fun getDatabaseUsers(): MutableList<User> {
        viewModelScope.launch {
            repository.getUsers()?.catch { exceptionHandler }?.collect { users ->
                dbusers = users.toMutableList()
            }
        }
        return dbusers
    }

    fun checkUserInDB(
        userName: String,
        password: String,
    ): Boolean {
        dbusers.forEach { activeUser ->
            if ((activeUser.userName == userName) && (activeUser.password == password)) {
                setLoggedIn(true)
                loggedInUserId = activeUser.id
                return true
            } else setLoggedIn(false)
        }
        return false
    }

    fun registerUser(registerUser: User) {
        viewModelScope.launch {
            val userId = repository.insertUserData(registerUser)
            loggedInUserId = userId.toInt()
        }
        setLoggedIn(true)
    }

    private fun setLoggedIn(boolean: Boolean) {
        hasLoggedIn.value = boolean
    }

}