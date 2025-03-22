package com.money.expenz.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.money.expenz.repository.UserRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory<T : ViewModel>(
    private val viewModelClass: Class<T>,
    private val repository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(viewModelClass) -> {
                viewModelClass.getConstructor(UserRepository::class.java)
                    .newInstance(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
    }
}
