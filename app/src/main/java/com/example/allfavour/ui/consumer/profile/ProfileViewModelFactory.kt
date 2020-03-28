package com.example.allfavour.ui.consumer.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.allfavour.data.ConsumerRepository
import com.example.allfavour.data.ProviderRepository
import com.example.allfavour.ui.provider.profile.ProfileProviderViewModel

class ProfileConsumerViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileConsumerViewModel::class.java)) {
            return ProfileConsumerViewModel(
                consumerRepository = ConsumerRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}