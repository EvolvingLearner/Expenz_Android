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
import com.money.expenz.utils.ExpenzUtil
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

    val viewState =
        hasLoggedIn.map { hasLoggedIn ->
            if (hasLoggedIn) {
                ViewState.LoggedIn
            } else {
                ViewState.NotLoggedIn
            }
        }

    var loggedInUserId = MutableLiveData(0)

    private var dbusers: MutableList<User> = mutableListOf()
    private var dbUsersWithIE: MutableList<UserWithIEDetails> = mutableListOf()

    var _loggedInUser = MutableLiveData<User>()
    val loggedInUser: LiveData<User> get() = _loggedInUser

    private var _ieDetails: MutableList<IEDetails> = mutableListOf()
    var ieDetailsList: List<IEDetails> = listOf()

    private var _selectedIEDetails = MutableLiveData<IEDetails>()
    val selectedIEDetails: LiveData<IEDetails> get() = _selectedIEDetails

    private var _userName = ""
    private var _password = ""

    private var job: Job? = null

    init {
        getDatabaseUsers()
    }

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            notifyError(throwable)
        }

    private fun getDatabaseUsers() {
        viewModelScope.launch {
            repository.getUsers()?.catch { exceptionHandler }
                ?.collect { users ->
                    dbusers = users.toMutableList()
                    users.forEach { loggedInUser ->
                        if (loggedInUser.userName == _userName && loggedInUser.password == _password) {
                            loggedInUserId.value = loggedInUser.id
                            _loggedInUser.value = loggedInUser
                            setLoggedIn(true)
                        } else setLoggedIn(false)
                    }
                }
        }
    }

    private fun getUserWithIEDetails() {
        job =
            viewModelScope.launch {
                repository.getUserWithIEDetails().flowOn(Dispatchers.IO).catch { exceptionHandler }
                    .collect { users ->
                        dbUsersWithIE = users.toMutableList()
                        users.forEach { activeUser ->
                            if ((activeUser.user.id == loggedInUserId.value)) {
                                setLoggedIn(true)
                                _loggedInUser.value = activeUser.user
                                _ieDetails = activeUser.ieDetails.toMutableList()
                            } else
                                setLoggedIn(false)
                        }
                    }
            }
    }

    private fun notifyError(exception: Throwable) {
        Log.d("ExpenzViewModel", "Exception ${exception.localizedMessage}")
    }

    fun checkUserInDB(
        userName: String,
        password: String,
    ): Boolean {
        getDatabaseUsers()
        dbusers.forEach { activeUser ->
            if ((activeUser.userName == userName) && (activeUser.password == password)) {
                setLoggedIn(true)
                getLoggedInUserDetails(activeUser.id)
                loggedInUserId.value = activeUser.id
                return true
            }
        }
        return false
    }

    private fun getLoggedInUserDetails(loggedInUserId: Int) {
        viewModelScope.launch {
            _loggedInUser.value = repository.getLoggedInUserDetails(loggedInUserId)
        }
        getUserWithIEDetails()
    }

    fun registerUser(registerUser: User) {
        _userName = registerUser.userName
        _password = registerUser.password
        viewModelScope.launch {
            repository.insertUserData(registerUser)
        }
        setLoggedIn(true)
    }

    fun insertIEDetails(ieDetails: IEDetails) {
        viewModelScope.launch { repository.insertIEDetails(ieDetails) }
    }

    fun updateUserDetails(
        amount: Int,
        ieValue: String,
    ) {
        if (ieValue == ExpenzUtil.INCOME) {
            _loggedInUser.value?.totalIncome = _loggedInUser.value?.totalIncome?.plus(amount)!!
        } else if (ieValue == ExpenzUtil.EXPENSE || ieValue == ExpenzUtil.SUBSCRIPTION) {
            _loggedInUser.value?.totalExpense = _loggedInUser.value?.totalExpense?.plus(amount)!!
        }
        viewModelScope.launch { _loggedInUser.value?.let { repository.updateUserDetails(it) } }
    }

    private fun setLoggedIn(boolean: Boolean) {
        hasLoggedIn.value = boolean
    }

    fun filterIEList(category: String) {
        ieDetailsList = _ieDetails.filter { item -> item.ie == category }
        // job = viewModelScope.launch { _ieDetails = repository.searchIECategory(category) }
    }

    fun getIEDetails(ieID: Int) {
        dbUsersWithIE.forEach {
            it.ieDetails.forEach { ie ->
                if (ie.ieId == ieID) {
                    _selectedIEDetails.value = ie
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
