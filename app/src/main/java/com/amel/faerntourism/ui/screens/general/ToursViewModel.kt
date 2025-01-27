package com.amel.faerntourism.ui.screens.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amel.faerntourism.data.TourRepository
import com.amel.faerntourism.data.model.Tour
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TourListViewState {
    data class Success(val tours: List<Tour>) : TourListViewState
    data class Error(val errorMsg: String) : TourListViewState
    data object Loading : TourListViewState
}

@HiltViewModel
class ToursViewModel @Inject constructor(
    private val tourRepository: TourRepository
) : ViewModel() {

    private val _internalTourListStateFlow =
        MutableStateFlow<TourListViewState>(TourListViewState.Loading)
    val tourListStateFlow = _internalTourListStateFlow.asStateFlow()

    fun getTours() {
        viewModelScope.launch {
            _internalTourListStateFlow.update { return@update TourListViewState.Loading }
            tourRepository.getTours().onSuccess { tours ->
                _internalTourListStateFlow.update {
                    return@update TourListViewState.Success(tours)
                }
            }.onFailure { e ->
                _internalTourListStateFlow.update {
                    return@update TourListViewState.Error(e.message ?: "Unknown error occurred.")
                }
            }
        }
    }
}
