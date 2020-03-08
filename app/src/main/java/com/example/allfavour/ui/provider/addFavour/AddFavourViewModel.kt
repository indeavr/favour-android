package com.example.allfavour.ui.provider.addFavour

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.data.FavourRepository
import com.example.allfavour.data.model.Favour
import kotlinx.coroutines.launch

class AddFavourViewModel(val favourRepository: FavourRepository) : ViewModel() {
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = this._status

    fun addFavour(favour: Favour) {
        viewModelScope.launch {
            val result = favourRepository.addFavour(favour)

            if (result == null) {
                _status.value = "failed"
            }
        }

    }
}
