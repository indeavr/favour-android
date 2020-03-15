package com.example.allfavour.ui.provider.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.allfavour.data.OfferingRepository
import com.example.allfavour.data.ProviderRepository
import com.example.allfavour.ui.consumer.search.OfferingsSearchViewModel


class ProfileViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileProviderViewModel::class.java)) {
            return ProfileProviderViewModel(
                providerRepository = ProviderRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}