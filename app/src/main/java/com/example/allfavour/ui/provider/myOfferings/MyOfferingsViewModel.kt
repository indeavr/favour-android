package com.example.allfavour.ui.provider.myOfferings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.data.ProviderRepository
import com.example.allfavour.data.model.ActiveOfferingModel
import com.example.allfavour.data.model.OfferingModel
import kotlinx.coroutines.launch

class MyOfferingsViewModel(private val providerRepository: ProviderRepository) : ViewModel() {
    private val _myOfferingsList = MutableLiveData<ArrayList<ActiveOfferingModel>>()
    val myOfferingsList: LiveData<ArrayList<ActiveOfferingModel>> = this._myOfferingsList

    fun getMyOfferings(userId: String) {
        viewModelScope.launch {
            val res = providerRepository.getMyOfferings(userId)

            if (res != null) {
                _myOfferingsList.value = res
            }
        }
    }

}
