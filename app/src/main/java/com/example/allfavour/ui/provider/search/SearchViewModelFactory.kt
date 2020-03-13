package com.example.allfavour.ui.provider.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.allfavour.data.FavourRepository

/**
 * ViewModel consumer factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class SearchViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(
                favourRepository = FavourRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
