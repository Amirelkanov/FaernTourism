package com.amel.faerntourism.data

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

interface LocationRepository {
    fun startLocationUpdates(): Flow<Location>
    fun stopLocationUpdates()
}

class LocationRepositoryImpl @Inject constructor(
    private val fusedClient: FusedLocationProviderClient
) : LocationRepository {

    private var locationCallback: LocationCallback? = null
    private val locationFlow = MutableSharedFlow<Location>(replay = 1)

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates(): Flow<Location> {

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(2000L)
            .setMaxUpdateDelayMillis(7000L)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.lastOrNull()?.let { loc ->
                    locationFlow.tryEmit(loc)
                }
            }
        }

        fusedClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            Looper.getMainLooper()
        )

        return locationFlow
    }

    override fun stopLocationUpdates() {
        locationCallback?.let {
            fusedClient.removeLocationUpdates(it)
        }
        locationCallback = null
    }
}
