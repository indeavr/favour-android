package com.example.allfavour.ui.consumer.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.allfavour.data.OfferingRepository

class OfferingsSearchViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OfferingsSearchViewModel::class.java)) {
            return OfferingsSearchViewModel(
                offeringRepository = OfferingRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}