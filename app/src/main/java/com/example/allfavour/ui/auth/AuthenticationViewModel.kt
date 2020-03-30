package com.example.allfavour.ui.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.data.AuthRepository
import com.example.allfavour.data.model.AuthModel
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _authModel = MutableLiveData<AuthModel>()
    val authModel: LiveData<AuthModel> = this._authModel

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = this._error

    lateinit var userId: String
    lateinit var email: String

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val authPayload = authRepository.login(email, password)
            userId = authPayload.userId
            _authModel.postValue(authPayload)
        }
    }

    fun loginWithGoogle(token: String) {
        viewModelScope.launch {
            val authPayload = authRepository.loginWithGoogle(token)
            userId = authPayload.userId
            _authModel.postValue(authPayload)
        }
    }

    fun register(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            try {
                val authPayload = authRepository.register(email, password, firstName, lastName)
                userId = authPayload.userId
                _authModel.postValue(authPayload)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    fun saveEmailBeforeTransition(email: String) {
        this.email = email
    }

    fun logout(context: Context) {
        // exposed in main activity (not sure why. It can be here i guess)
    }
}