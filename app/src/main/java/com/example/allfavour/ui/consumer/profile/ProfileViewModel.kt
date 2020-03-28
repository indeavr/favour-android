package com.example.allfavour.ui.consumer.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.data.ConsumerRepository
import com.example.allfavour.data.ProviderRepository
import com.example.allfavour.data.model.ConsumerModel
import com.example.allfavour.data.model.ProviderModel
import kotlinx.coroutines.launch

class ProfileConsumerViewModel(val consumerRepository: ConsumerRepository) : ViewModel() {
    lateinit var userId: String

    private val _consumer = MutableLiveData<ConsumerModel>()
    val consumer: LiveData<ConsumerModel> = this._consumer

    fun getConsumer() {
        if (this.userId == null) {
            return
        }
        viewModelScope.launch {
            val result = consumerRepository.getConsumer(userId)

            if (result != null) {
                _consumer.value = result
            }
        }
    }
}
