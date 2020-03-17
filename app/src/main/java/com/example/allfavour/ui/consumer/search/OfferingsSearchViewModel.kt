package com.example.allfavour.ui.consumer.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.data.OfferingRepository
import com.example.allfavour.data.model.ApplicationModel
import com.example.allfavour.data.model.OfferingModel
import kotlinx.coroutines.launch

class OfferingsSearchViewModel(private val offeringRepository: OfferingRepository) : ViewModel() {
    private val _offeringsList = MutableLiveData<ArrayList<OfferingModel>>()
    val offeringsList: LiveData<ArrayList<OfferingModel>> = this._offeringsList

    private val _currentOffering = MutableLiveData<OfferingModel>()
    val currentOffering: LiveData<OfferingModel> = this._currentOffering

    private val _appliedSuccessfully = MutableLiveData<Boolean?>()
    val appliedSuccessfully: LiveData<Boolean?> = this._appliedSuccessfully

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

    fun applyForOffering(userId: String, application: ApplicationModel) {
        val offeringId = currentOffering.value?.id

        if (userId != null && offeringId != null) {
            viewModelScope.launch {
                val result = offeringRepository.applyForOffering(userId, offeringId, application)
                _appliedSuccessfully.value = result
            }
        }
    }
}
