package com.example.allfavour.ui.oldauth

import android.util.Patterns
import androidx.lifecycle.*
import com.example.allfavour.data.AuthRepository

import com.example.allfavour.R
import com.example.allfavour.data.model.AuthModel
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private var _loginResult = MutableLiveData<AuthModel>()
    val loginResult: LiveData<AuthModel> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.login(username, password)
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
