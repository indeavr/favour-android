package com.example.allfavour

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.data.LoginRepository
import com.example.allfavour.ui.auth.LoggedUser
import kotlinx.coroutines.launch

class MainActivityViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    private val _registeredUser = MutableLiveData<LoggedUser>()
    val registeredUser: LiveData<LoggedUser> = this._registeredUser

    fun loginWithGoogle(username: String, token: String) {
        viewModelScope.launch {
            val loggedUser = loginRepository.loginWithGoogle(username, token)

            _registeredUser.postValue(
                LoggedUser(
                    loggedUser.userId,
                    loggedUser.displayName,
                    loggedUser.token
                )
            )
        }
    }

}