package com.money.expenz.ui.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.money.expenz.data.IEDetails
import com.money.expenz.data.Subscription
import com.money.expenz.data.User
import com.money.expenz.data.UserWithIEDetails
import com.money.expenz.repository.UserRepository
import com.money.expenz.utils.ExpenzUtil.Companion.EXPENSE
import com.money.expenz.utils.ExpenzUtil.Companion.INCOME
import com.money.expenz.utils.ExpenzUtil.Companion.SUBSCRIPTION
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ExpenzViewModel for communication with database, DAO , Repositories
 * to provide livedata objects to handle UI events
 */

class ExpenzViewModel(
    private val repository: UserRepository,
) : ViewModel() {
    sealed class ViewState {
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

    sealed class LoadingState {
        object Loading : LoadingState()
        object Success : LoadingState()
        object Error : LoadingState()
        object Idle : LoadingState()
    }

    private val progressBarLoadingState = mutableStateOf<LoadingState>(LoadingState.Idle)
    val loadingState: State<LoadingState> = progressBarLoadingState

    var showDialog by mutableStateOf(false)
        private set

    var dialogTitle by mutableStateOf("Alert")
        private set

    var dialogMessage by mutableStateOf("Are you sure?")
        private set

    var onYesClicked: (() -> Unit)? = null
    var onNoClicked: (() -> Unit)? = null
    var iedetailsToEdit: IEDetails? = null

    var loggedInUserId = 0

    private var dbusers: MutableList<User> = mutableListOf()
    private var dbUsersWithIE: MutableList<UserWithIEDetails> = mutableListOf()

    var loggedInUserData = MutableStateFlow<User?>(null)
    val loggedInUser: StateFlow<User?> = loggedInUserData

    private var ieDetailsListData: MutableList<IEDetails> = mutableListOf()
    var ieDetailsList: List<IEDetails> = listOf()

    private var selectedIEDetailsData = MutableLiveData<IEDetails>()
    val selectedIEDetails: LiveData<IEDetails> get() = selectedIEDetailsData

    private val subscriptionListData = MutableStateFlow<List<Subscription>>(emptyList())
    val subscriptionList: StateFlow<List<Subscription>> get() = subscriptionListData

    // Error handling using StateFlow
    private val errorStateData = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> get() = errorStateData

    private var currentUserName = ""
    private var currentUserPassword = ""

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
            repository
                .getUsers()
                ?.catch { exceptionHandler }
                ?.collect { users ->
                    dbusers = users.toMutableList()
                    users.forEach { loggedInUser ->
                        if (loggedInUser.userName == currentUserName && loggedInUser.password == currentUserPassword) {
                            loggedInUserId = loggedInUser.id
                            loggedInUserData.value = loggedInUser
                            setLoggedIn(true)
                        } else {
                            setLoggedIn(false)
                        }
                    }
                }
        }
    }

    fun getUserWithIEDetails() {
        progressBarLoadingState.value = LoadingState.Loading
        job =
            viewModelScope.launch {
                repository
                    .getUserWithIEDetails()
                    .flowOn(Dispatchers.IO)
                    .catch { exceptionHandler }
                    .collect { users ->
                        dbUsersWithIE = users.toMutableList()
                        users.forEach { activeUser ->
                            if ((activeUser.user.id == loggedInUserId)) {
                                setLoggedIn(true)
                                loggedInUserData.value = activeUser.user
                                ieDetailsListData = activeUser.ieDetails.toMutableList()
                            } else {
                                setLoggedIn(false)
                            }
                            progressBarLoadingState.value = LoadingState.Success
                        }
                    }
            }
    }

    private fun notifyError(exception: Throwable) {
        Log.d("ExpenzViewModel", "Exception ${exception.localizedMessage}")
        progressBarLoadingState.value = LoadingState.Error
        errorStateData.value = "Error: ${exception.message}"
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
                loggedInUserId = activeUser.id
                return true
            }
        }
        return false
    }

     fun getLoggedInUserDetails(loggedInUserId: Int) {
        progressBarLoadingState.value = LoadingState.Loading
         getUserWithIEDetails()
        viewModelScope.launch {
            loggedInUserData.value = repository.getLoggedInUserDetails(loggedInUserId)
        }

    }

    fun registerUser(registerUser: User) {
        currentUserName = registerUser.userName
        currentUserPassword = registerUser.password
        viewModelScope.launch {
            repository.insertUserData(registerUser)
        }
        setLoggedIn(true)
    }

    fun insertIEDetails(ieDetails: IEDetails) {
        viewModelScope.launch { repository.insertIEDetails(ieDetails) }
        if (ieDetails.ie == SUBSCRIPTION) {
            val subscription =
                Subscription(
                    category = ieDetails.category,
                    amount = ieDetails.amount,
                    date = ieDetails.date,
                    notes = ieDetails.notes,
                    userId = ieDetails.userId,
                )
            insertSubscription(subscription)
        }
    }

    fun insertSubscription(subscription: Subscription) {
        viewModelScope.launch { repository.insertSubscription(subscription) }
    }

    fun updateUserIEAmount(
        amount: Int,
        ieValue: String,
        isDeleted: Boolean
    ) {
        progressBarLoadingState.value = LoadingState.Loading
        loggedInUserData.value?.let { user ->
            val adjustmentAmount = if (isDeleted) -amount else amount
            when (ieValue) {
                INCOME -> {
                    user.totalIncome = user.totalIncome + adjustmentAmount
                }

                EXPENSE, SUBSCRIPTION -> {
                    user.totalExpense = user.totalExpense + adjustmentAmount
                    if (ieValue == SUBSCRIPTION) {
                        user.totalSubscription = user.totalSubscription + adjustmentAmount
                    }
                }
            }
            updateUserDetails(user)
        }
        progressBarLoadingState.value = LoadingState.Success
    }

    fun updateIEDetails(ieDetails: IEDetails) {
        progressBarLoadingState.value = LoadingState.Loading
        viewModelScope.launch {
            repository.updateIEDetails(ieDetails)
        }
        progressBarLoadingState.value = LoadingState.Success
    }

    fun updateUserDetails(user: User) {
        viewModelScope.launch { repository.updateUserDetails(user) }
        getUserWithIEDetails()
    }

    private fun setLoggedIn(boolean: Boolean) {
        hasLoggedIn.value = boolean
    }

    fun filterIEList(category: String) {
        ieDetailsList =
            when (category) {
                EXPENSE -> ieDetailsListData.filter { it.ie == EXPENSE || it.ie == SUBSCRIPTION }
                INCOME -> ieDetailsListData.filter { it.ie == INCOME }
                else -> emptyList()
            }
    }

    fun getIEDetails(ieID: Int) {
        dbUsersWithIE.forEach {
            it.ieDetails.forEach { ie ->
                if (ie.ieId == ieID) {
                    selectedIEDetailsData.value = ie
                    iedetailsToEdit = ie
                }
            }
        }
    }

    fun getSubscriptionList() {
        viewModelScope.launch {
            repository
                .getAllSubscriptions()
                .catch { exception ->
                    // Handle the exception (e.g., update error state)
                    errorStateData.value = "Error: ${exception.message}"
                }.collect { subscriptions ->
                    // Collect the list of subscriptions and update the StateFlow
                    subscriptionListData.value = subscriptions
                }
        }
    }

    fun deleteIE() {
        selectedIEDetailsData.value?.let {
            viewModelScope.launch { repository.deleteIE(it) }
            updateUserIEAmount(it.amount, it.ie, true)
        }
    }

    fun showDialog(
        title: String,
        message: String,
        onYes: () -> Unit,
        onNo: () -> Unit
    ) {
        dialogTitle = title
        dialogMessage = message
        onYesClicked = onYes
        onNoClicked = onNo
        showDialog = true
    }

    fun hideDialog() {
        showDialog = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
