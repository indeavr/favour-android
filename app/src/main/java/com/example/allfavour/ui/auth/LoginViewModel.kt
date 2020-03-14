package com.example.allfavour.ui.auth

import android.accounts.AccountManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.data.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: LoginRepository) : ViewModel() {

    private val _registeredUser = MutableLiveData<LoggedUser>()
    val registeredUser: LiveData<LoggedUser> = this._registeredUser

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val loggedUser = authRepository.login(email, password)
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
