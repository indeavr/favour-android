package com.example.allfavour.ui.provider.addFavour

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.data.FavourRepository
import com.example.allfavour.data.model.Favour
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.launch

class AddFavourViewModel(val favourRepository: FavourRepository) : ViewModel() {
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = this._status
    var place: Place? = null

    fun addFavour(favour: Favour) {
        viewModelScope.launch {
            favourRepository.addFavour(favour)

        }

    }
}
