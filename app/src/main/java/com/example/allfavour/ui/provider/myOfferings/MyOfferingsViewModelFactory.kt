package com.example.allfavour.ui.provider.myOfferings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.allfavour.data.OfferingRepository
import com.example.allfavour.data.ProviderRepository
import com.example.allfavour.ui.consumer.search.OfferingsSearchViewModel

class MyOfferingsViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyOfferingsViewModel::class.java)) {
            return MyOfferingsViewModel(
                providerRepository = ProviderRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}