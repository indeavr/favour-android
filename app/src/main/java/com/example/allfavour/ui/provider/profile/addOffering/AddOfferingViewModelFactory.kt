package com.example.allfavour.ui.provider.profile.addOffering

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.allfavour.data.FavourRepository
import com.example.allfavour.data.OfferingRepository

class AddOfferingViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddOfferingViewModel::class.java)) {
            return AddOfferingViewModel(
                offeringRepository = OfferingRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
