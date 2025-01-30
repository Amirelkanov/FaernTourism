package com.amel.faerntourism.ui

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amel.faerntourism.data.LocationRepository
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _internalLocationState = MutableStateFlow<Location?>(null)
    val locationState: StateFlow<Location?> = _internalLocationState.asStateFlow()

    private var locationJob: Job? = null

    fun startTracking() {
        if (locationJob == null || locationJob?.isActive == false) {
            locationJob = viewModelScope.launch {
                locationRepository.startLocationUpdates()
                    .collect { newLocation ->
                        _internalLocationState.value = newLocation
                    }
            }
        }
    }

    fun stopTracking() {
        locationRepository.stopLocationUpdates()
        locationJob?.cancel()
        locationJob = null
    }

    companion object {
        fun GeoPoint.toLocation(): Location =
            Location("").apply {
                latitude = this@toLocation.latitude
                longitude = this@toLocation.longitude
            }

        fun prettifyDistance(distanceInMeters: Float): String =
            if (distanceInMeters < 1000f) "${distanceInMeters.roundToInt()} м" else "${(distanceInMeters / 1000).roundToInt()} км"
    }
}


