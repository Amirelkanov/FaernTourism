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

sealed interface PlaceListViewState {
    data class Success(val places: List<Place>) : PlaceListViewState
    data class Error(val errorMsg: String) : PlaceListViewState
    data object Loading : PlaceListViewState
}

sealed interface PlaceViewState {
    data class Success(val place: Place) : PlaceViewState
    data class Error(val errorMsg: String) : PlaceViewState
    data object Loading : PlaceViewState
}


@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val fireStoreRepository: FireStoreRepository
) : ViewModel() {
    private val _internalPlaceListViewStateFlow =
        MutableStateFlow<PlaceListViewState>(PlaceListViewState.Loading)
    val placeListViewStateFlow = _internalPlaceListViewStateFlow.asStateFlow()

    private val _internalPlaceViewStateFlow =
        MutableStateFlow<PlaceViewState>(PlaceViewState.Loading)
    val placeViewStateFlow = _internalPlaceViewStateFlow.asStateFlow()

    fun getPlacesData() {
        viewModelScope.launch {
            _internalPlaceListViewStateFlow.update { return@update PlaceListViewState.Loading }
            fireStoreRepository.getPlaces().onSuccess { places ->
                _internalPlaceListViewStateFlow.update {
                    return@update PlaceListViewState.Success(places)
                }
            }.onFailure { e ->
                _internalPlaceListViewStateFlow.update {
                    return@update PlaceListViewState.Error(e.message ?: "Unknown error occurred.")
                }
            }
        }
    }

    fun getPlace(id: String) = viewModelScope.launch {
        _internalPlaceViewStateFlow.update { return@update PlaceViewState.Loading }
        fireStoreRepository.getPlace(id).onSuccess { place ->
            _internalPlaceViewStateFlow.update {
                return@update PlaceViewState.Success(place)
            }
        }.onFailure { e ->
            _internalPlaceViewStateFlow.update {
                return@update PlaceViewState.Error(e.message ?: "Unknown error occurred.")
            }
        }
    }
}