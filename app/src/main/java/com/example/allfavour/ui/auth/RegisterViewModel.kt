package com.example.allfavour.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.data.LoginRepository
import com.example.allfavour.data.model.LoggedUser
import kotlinx.coroutines.launch
import kotlin.math.log

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
                    loggedUser.fullName,
                    loggedUser.permissions
                )
            )
        }
    }
}
