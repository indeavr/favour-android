package com.example.allfavour.ui.provider.profile.addOffering

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.data.OfferingRepository
import com.example.allfavour.data.model.OfferingModel
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.launch

class AddOfferingViewModel(val offeringRepository: OfferingRepository) : ViewModel() {
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = this._status
    var place: Place? = null

    fun addOffering(userId: String, offering: OfferingModel) {
        viewModelScope.launch {
            offeringRepository.addOffering(userId, offering)

        }

    }
}