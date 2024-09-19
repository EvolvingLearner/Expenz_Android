package com.money.expenz.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.money.expenz.data.IEDetails
import com.money.expenz.data.User
import com.money.expenz.data.UserWithIEDetails
import com.money.expenz.repository.UserRepository
import com.money.expenz.utils.Constants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ExpenzViewModel for communication with database, DAO , Repositories
 * to provide livedata objects to handle UI events
 */

class ExpenzViewModel(private val repository: UserRepository) : ViewModel() {

    sealed class ViewState {
        object Loading : ViewState() // hasLoggedIn = unknown
        object LoggedIn : ViewState() // hasLoggedIn = true
        object NotLoggedIn : ViewState() // hasLoggedIn = false
    }

    private val hasLoggedIn = MutableStateFlow(false)

    val viewState = hasLoggedIn.map { hasLoggedIn ->
        if (hasLoggedIn) {
            ViewState.LoggedIn
        } else {
            ViewState.NotLoggedIn
        }
    }

    var loggedInUserId = MutableLiveData(0)

    private var dbusers: MutableList<UserWithIEDetails> = mutableListOf()

    var _loggedInUser = MutableLiveData<User>()
    val loggedInUser: LiveData<User> get() = _loggedInUser

    var userName = ""
    var password = ""

    private var job: Job? = null

    init {
        getUserWithIEDetails()
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        notifyError(throwable)
    }

    private fun getUserWithIEDetails() {
        job = viewModelScope.launch {
            repository.getUserWithIEDetails().flowOn(Dispatchers.IO).catch { exceptionHandler }
                .collect { users ->
                    dbusers = users.toMutableList()
                    dbusers.forEach { activeUsers ->
                        if ((activeUsers.user.userName == userName) && (activeUsers.user.password == password)) {
                            setLoggedIn(true)
                            _loggedInUser.value = activeUsers.user
                        }
                    }
                }
        }
    }

    private fun notifyError(exception: Throwable) {
        Log.d("ExpenzViewModel", "Exception ${exception.localizedMessage}")
    }

    fun checkUserInDB(userName: String, password: String): Boolean {
        dbusers.forEach { activeUsers ->
            activeUsers.user
            if ((activeUsers.user.userName == userName) && (activeUsers.user.password == password)) {
                setLoggedIn(true)
                getLoggedInUserDetails(activeUsers.user.id)
                loggedInUserId.value = activeUsers.user.id
                return true
            }
        }
        return false
    }

    fun getLoggedInUserDetails(loggedInUserId: Int) {
        viewModelScope.launch {
            _loggedInUser.value = repository.getLoggedInUserDetails(loggedInUserId)
        }
    }

    fun registerUser(registerUser: User) {
        userName = registerUser.userName
        password = registerUser.password
        viewModelScope.launch {
            repository.insertUserData(registerUser)
        }
        setLoggedIn(true)
    }

    fun insertIEDetails(ieDetails: IEDetails) {
        viewModelScope.launch { repository.insertIEDetails(ieDetails) }
        getUserWithIEDetails()
    }

    fun updateUserDetails(amount: Int, ieValue: String) {
        if (ieValue == Constants.income) {
            _loggedInUser.value?.totalIncome = _loggedInUser.value?.totalIncome?.plus(amount)!!
        } else if (ieValue == Constants.expense || ieValue == Constants.subscription) {
            _loggedInUser.value?.totalExpense = _loggedInUser.value?.totalExpense?.plus(amount)!!
        }
        viewModelScope.launch { _loggedInUser.value?.let { repository.updateUserDetails(it) } }
    }

    fun setLoggedIn(boolean: Boolean) {
        hasLoggedIn.value = boolean
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
