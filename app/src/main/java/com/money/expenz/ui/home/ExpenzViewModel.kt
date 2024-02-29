package com.money.expenz.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.money.expenz.data.User
import com.money.expenz.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ExpenzViewModel for communication with database, DAO , Repositories
 * to provide livedata objects to handle UI events
 */

class ExpenzViewModel(private val repository: UserRepository) : ViewModel() {

    sealed class ViewState {
        object Loading: ViewState() // hasLoggedIn = unknown
        object LoggedIn: ViewState() // hasLoggedIn = true
        object NotLoggedIn: ViewState() // hasLoggedIn = false
    }

    private val hasLoggedIn = MutableStateFlow(false)

    val viewState = hasLoggedIn.map { hasLoggedIn ->
        if (hasLoggedIn) {
            ViewState.LoggedIn
        } else {
            ViewState.NotLoggedIn
        }
    }

    private var _isUserLoggedIn  = MutableLiveData(false)

    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    private var dbusers: MutableList<User> = mutableListOf()

    init {
        viewModelScope.launch {
            // Trigger the flow and consume its elements using collect
            repository.userData.catch { exception -> notifyError(exception) }
                .collect { users ->
                    dbusers = users as MutableList<User>
                }
        }
    }

    private fun notifyError(exception: Throwable) {
        Log.d("Expenz", "Exception $exception")
    }


    fun checkUserInDB(userName: String, password: String): Boolean {
        dbusers.forEach { user ->
           if ((user.userName == userName) && (user.password == password)) {
               _isUserLoggedIn.value = true
               setLoggedIn(true)
               return true
           }
        }
        return false
    }

    fun registerUser(registerUser: User) {
        viewModelScope.launch { repository.insertUserData(registerUser) }
        _isUserLoggedIn.value = true
        setLoggedIn(true)
    }

    fun setLoggedIn(boolean: Boolean){
        hasLoggedIn.value = boolean
    }
}