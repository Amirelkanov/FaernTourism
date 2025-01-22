package com.example.faerntourism.ui.screens.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faerntourism.data.FireStoreRepository
import com.example.faerntourism.data.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PlacesViewState {
    data class Success(val places: List<Place>) : PlacesViewState
    data class Error(val errorMsg: String) : PlacesViewState
    data object Loading : PlacesViewState
}

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val fireStoreRepository: FireStoreRepository
) : ViewModel() {
    private val _internalPlacesViewStateFlow =
        MutableStateFlow<PlacesViewState>(PlacesViewState.Loading)
    val placesViewStateFlow = _internalPlacesViewStateFlow.asStateFlow()

    init {
        getPlacesData()
    }

    fun getPlacesData() {
        viewModelScope.launch {
            _internalPlacesViewStateFlow.update { return@update PlacesViewState.Loading }
            fireStoreRepository.getPlaces().onSuccess { places ->
                _internalPlacesViewStateFlow.update {
                    return@update PlacesViewState.Success(places)
                }
            }.onFailure { e ->
                _internalPlacesViewStateFlow.update {
                    return@update PlacesViewState.Error(e.message ?: "Unknown error occurred.")
                }
            }
        }
    }
}