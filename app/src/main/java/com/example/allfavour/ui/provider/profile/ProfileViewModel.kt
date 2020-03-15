package com.example.allfavour.ui.provider.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.data.ProviderRepository
import com.example.allfavour.data.model.ProviderModel
import kotlinx.coroutines.launch

class ProfileProviderViewModel(val providerRepository: ProviderRepository) : ViewModel() {
    private val _provider = MutableLiveData<ProviderModel>()
    val provider: LiveData<ProviderModel> = this._provider

    fun getProvider(userId: String) {
        if (userId == null) {
            return
        }
        viewModelScope.launch {
            val result = providerRepository.getProvider(userId)

            if (result != null) {
                _provider.value = result
            }
        }
    }
}
