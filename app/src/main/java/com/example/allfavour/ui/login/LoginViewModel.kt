package com.example.allfavour.ui.login

import android.util.Patterns
import androidx.lifecycle.*
import com.example.allfavour.data.LoginRepository
import com.example.allfavour.data.Result

import com.example.allfavour.R
import com.example.allfavour.data.model.LoggedInUser
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private var _loginResult = MutableLiveData<LoggedInUser>()
    val loginResult: LiveData<LoggedInUser> = _loginResult

    fun register(username: String, password: String) {
        viewModelScope.launch {
            val result = loginRepository.register(username, password)
            _loginResult.value = result
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5;
    }
}
