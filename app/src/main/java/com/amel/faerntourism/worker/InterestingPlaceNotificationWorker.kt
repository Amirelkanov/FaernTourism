package com.amel.faerntourism.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.amel.faerntourism.data.FireStoreRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class InterestingPlaceNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val fireStoreRepository: FireStoreRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val placesResult = fireStoreRepository.getPlaces()

        placesResult.onSuccess { places ->
            if (places.isNotEmpty()) {
                val randomPlace = places.random()

                makeInterestingPlaceNotification(randomPlace, applicationContext)

                return Result.success()
            } else {
                return Result.failure()
            }
        }.onFailure {
            return Result.failure()
        }
        return Result.failure()
    }
}
