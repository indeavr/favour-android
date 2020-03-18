package com.example.allfavour.ui.consumer.basicForm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.allfavour.data.ConsumerRepository


class BasicInfoConsumerFormViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BasicInfoConsumerFormViewModel::class.java)) {
            return BasicInfoConsumerFormViewModel(
                consumerRepository = ConsumerRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}