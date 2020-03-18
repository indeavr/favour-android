package com.example.allfavour.ui.provider.basicForm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.allfavour.data.ProviderRepository

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