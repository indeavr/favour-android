package com.example.allfavour.ui.auth

import android.accounts.AccountManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.data.LoginRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: LoginRepository) : ViewModel() {

    private val _registeredUser = MutableLiveData<LoggedUser>()
    val registeredUser: LiveData<LoggedUser> = this._registeredUser

    fun register(email: String, password: String, firstName: String, lastName: String) {


        viewModelScope.launch {
            val loggedUser = authRepository.register(email, password, firstName, lastName)
            _registeredUser.postValue(
                LoggedUser(
                    loggedUser.userId,
                    loggedUser.displayName,
                    loggedUser.token,
                    loggedUser.fullName
                )
            )
        }
    }
}
