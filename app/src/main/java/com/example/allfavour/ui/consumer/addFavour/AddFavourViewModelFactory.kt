package com.example.allfavour.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.allfavour.data.FavourRepository
import com.example.allfavour.ui.consumer.addFavour.AddFavourViewModel

/**
 * ViewModel consumer factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class AddFavourViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddFavourViewModel::class.java)) {
            return AddFavourViewModel(
                favourRepository = FavourRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
