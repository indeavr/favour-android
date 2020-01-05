package com.example.allfavour.ui.login

import android.util.Patterns
import androidx.lifecycle.*
import com.example.allfavour.data.LoginRepository
import com.example.allfavour.data.Result

import com.example.allfavour.R
import com.example.allfavour.data.model.LoggedInUser

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private var _loginResult = MutableLiveData<LoggedInUser>()
    val loginResult: LiveData<LoggedInUser> = _loginResult

    init {
        _loginResult = loginRepository.loggedInUser
    }

    fun login(username: String, password: String) {
//        _loginResult.switchMap{
//            liveData(context = viewModelScope.coroutineContext + Dispatcher.IO) {
//
//            }
//        }
        // can be launched in a separate asynchronous job
        _loginResult.switchMap {
            liveData {
                val data = loginRepository.login(username, password) // loadUser is a suspend function.
                emit(data)
            }
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
