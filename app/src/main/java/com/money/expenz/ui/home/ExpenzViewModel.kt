package com.money.expenz.ui.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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
import com.money.expenz.utils.ExpenzUtil
import com.money.expenz.utils.ExpenzUtil.Companion.EXPENSE
import com.money.expenz.utils.ExpenzUtil.Companion.INCOME
import com.money.expenz.utils.ExpenzUtil.Companion.SUBSCRIPTION
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ExpenzViewModel for communication with database, DAO , Repositories
 * to provide livedata objects to handle UI events
 */

class ExpenzViewModel(
    private val repository: UserRepository,
) : ViewModel() {

    enum class LoadingState {
        Loading,
        Success,
        Error
    }

    val progressBarLoadingState = MutableStateFlow(LoadingState.Loading)
    val loadingState: StateFlow<LoadingState> = progressBarLoadingState

    var showDialog by mutableStateOf(false)
        private set

    var dialogTitle by mutableStateOf("Alert")
        private set

    var dialogMessage by mutableStateOf("Are you sure?")
        private set

    var onYesClicked: (() -> Unit)? = null
    var onNoClicked: (() -> Unit)? = null
    var iedetailsToEdit: IEDetails? = null

    var loggedInUserData = MutableLiveData<User>()
    val loggedInUser: LiveData<User> = loggedInUserData
    val user = MutableStateFlow<User?>(null)

    private var ieDetailsListData: MutableList<IEDetails> = mutableListOf()

    private val selectedCategory = MutableStateFlow<String?>(null)

    private val expenzTypeListData = MutableStateFlow<List<IEDetails>>(emptyList())

    private var selectedIEDetailsData = MutableLiveData<IEDetails>()
    val selectedIEDetails: LiveData<IEDetails> get() = selectedIEDetailsData

    private val subscriptionListData = MutableStateFlow<List<Subscription>>(emptyList())
    val subscriptionList: StateFlow<List<Subscription>> get() = subscriptionListData

    private var selectedSubscriptionDetailsData = MutableLiveData<Subscription>()
    val selectedSubscriptionDetails: LiveData<Subscription> get() = selectedSubscriptionDetailsData

    // Error handling using StateFlow
    private val errorStateData = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> get() = errorStateData

    private var currentUserName = ""
    private var currentUserPassword = ""

    private var job: Job? = null

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            notifyError(throwable)
        }

    val filteredExpenzTypeList: StateFlow<List<IEDetails>> =
        combine(expenzTypeListData, selectedCategory) { list, category ->
            when (category) {
                EXPENSE -> list.filter { it.ie == EXPENSE || it.ie == SUBSCRIPTION }
                INCOME -> list.filter { it.ie == INCOME }
                null -> list // Show all if no category is selected
                else -> emptyList()
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun getUserWithIEDetails() {
        progressBarLoadingState.value = LoadingState.Loading
        job = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val userWithIEDetails =
                repository.getUserWithIEDetails(ExpenzUtil.UserSession.userId ?: 0)
            loggedInUserData.postValue(userWithIEDetails.user)
            ieDetailsListData = userWithIEDetails.ieDetails.toMutableList()
            expenzTypeListData.value = userWithIEDetails.ieDetails
            progressBarLoadingState.value = LoadingState.Success
        }
    }

    private fun notifyError(exception: Throwable) {
        Log.d("ExpenzViewModel", "Exception ${exception.localizedMessage}")
        progressBarLoadingState.value = LoadingState.Error
        errorStateData.value = "Error: ${exception.message}"
    }

    fun getLoggedInUserDetails(loggedInUserId: Int) {
        progressBarLoadingState.value = LoadingState.Loading
        viewModelScope.launch {
            val currentuser = repository.getLoggedInUserDetails(loggedInUserId)
            user.value = currentuser
            loggedInUserData.value = currentuser
        }
        progressBarLoadingState.value = LoadingState.Success
    }

    fun insertIEDetails(ieDetails: IEDetails) {
        progressBarLoadingState.value = LoadingState.Loading
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
        //getUserWithIEDetails()
    }

    fun updateUserDetails(user: User) {
        viewModelScope.launch { repository.updateUserDetails(user) }
    }

    fun setFilter(category: String?) {
        selectedCategory.value = category
    }

    fun getIEDetails(ieID: Int) {
        ieDetailsListData.forEach { ie ->
            if (ie.ieId == ieID) {
                selectedIEDetailsData.value = ie
                iedetailsToEdit = ie
            }
        }
    }

    fun getSubscriptionDetails(subscriptionID: Int) {
        viewModelScope.launch {
            subscriptionListData.collect { subscriptionList ->
                subscriptionList.forEach { subscription ->
                    if (subscription.subscriptionId == subscriptionID) {
                        selectedSubscriptionDetailsData.value = subscription
                    }
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
