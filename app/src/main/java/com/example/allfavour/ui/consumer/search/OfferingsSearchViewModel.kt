package com.example.allfavour.ui.consumer.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.data.OfferingRepository
import com.example.allfavour.data.model.Offering
import kotlinx.coroutines.launch

class OfferingsSearchViewModel(private val offeringRepository: OfferingRepository) : ViewModel() {
    private val _offeringsList = MutableLiveData<ArrayList<Offering>>()
    val offeringsList: LiveData<ArrayList<Offering>> = this._offeringsList

    private val _currentOffering = MutableLiveData<Offering>()
    val currentOffering: LiveData<Offering> = this._currentOffering

    fun getOfferings() {
        viewModelScope.launch {
            val result = offeringRepository.getOfferings()
            _offeringsList.value = result
        }
    }

    fun setCurrentOffering(id: String) {
        if (id.isEmpty()) {
            return
        }

        _currentOffering.value = _offeringsList.value!!.find { it.id == id }
    }
}
