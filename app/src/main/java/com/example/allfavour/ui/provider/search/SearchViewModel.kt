package com.example.allfavour.ui.provider.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.data.FavourRepository
import com.example.allfavour.data.model.Favour
import kotlinx.coroutines.launch

class SearchViewModel(private val favourRepository: FavourRepository) : ViewModel() {
    private val _favoursList = MutableLiveData<ArrayList<Favour>>()
    val favoursList: LiveData<ArrayList<Favour>> = this._favoursList
    var currentFavour: Favour? = null


    fun getFavours() {
        viewModelScope.launch {
            val result = favourRepository.getFavours()
            _favoursList.value = result
        }
    }

    fun setCurrentFavour(id: String) {
        currentFavour = favoursList.value!!.find { it.id == id }
    }
}
