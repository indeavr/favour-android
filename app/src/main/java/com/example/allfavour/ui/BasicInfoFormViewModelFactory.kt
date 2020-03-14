package com.example.allfavour.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.allfavour.data.OfferingRepository
import com.example.allfavour.data.ProviderRepository
import com.example.allfavour.ui.consumer.search.OfferingsSearchViewModel

class BasicInfoFormViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BasicInfoFormViewModel::class.java)) {
            return BasicInfoFormViewModel(
                providerRepository = ProviderRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}