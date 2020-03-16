package com.example.allfavour.ui.auth

import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.MainActivity
import com.example.allfavour.data.LoginRepository
import com.example.allfavour.data.model.LoggedUser
import com.example.allfavour.services.authentication.AuthenticationProvider
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val authRepository: LoginRepository) : ViewModel() {

    private val _registeredUser = MutableLiveData<LoggedUser>()
    val registeredUser: LiveData<LoggedUser> = this._registeredUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = this._error

    lateinit var userId: String
    lateinit var email: String

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val loggedUser = authRepository.login(email, password)
            userId = loggedUser.userId
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

    fun loginWithGoogle(username: String, token: String) {
        viewModelScope.launch {
            val loggedUser = authRepository.loginWithGoogle(username, token)
            userId = loggedUser.userId
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

    fun register(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            try {
                val loggedUser = authRepository.register(email, password, firstName, lastName)
                userId = loggedUser.userId
                _registeredUser.postValue(
                    LoggedUser(
                        loggedUser.userId,
                        loggedUser.displayName,
                        loggedUser.token,
                        loggedUser.fullName,
                        loggedUser.permissions
                    )
                )
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    fun saveEmailBeforeTransition(email: String) {
        this.email = email
    }

    fun logout(context: Context) {
        val accountManager = AccountManager.get(context.applicationContext)
        val accounts = accountManager.getAccountsByType("AllFavour")
        val oldToken = AuthenticationProvider.getAuthToken(context.applicationContext)
        accountManager.invalidateAuthToken("AllFavour", oldToken)
        AuthenticationProvider.invalidateToken(context.applicationContext)
        accountManager.clearPassword(accounts[0])

    }
}