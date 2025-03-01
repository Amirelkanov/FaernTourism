package com.amel.faerntourism.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.rustore.sdk.review.RuStoreReviewManager
import ru.rustore.sdk.review.model.ReviewInfo
import javax.inject.Inject
import ru.rustore.sdk.review.errors.RuStoreReviewExists as RuStoreReviewExistsException

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewManager: RuStoreReviewManager
) : ViewModel() {

    private val _event = MutableSharedFlow<UserFlowEvent>(
        extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event = _event.asSharedFlow()

    private var reviewInfo: ReviewInfo? = null
    private var reviewAlreadyExists = false

    init {
        requestReviewFlow()
    }

    private fun requestReviewFlow() {
        if (reviewInfo != null) return
        reviewManager.requestReviewFlow().addOnSuccessListener { newReviewInfo ->
            reviewInfo = newReviewInfo
            Log.d(TAG, "Successfully requested review info.")
        }.addOnFailureListener { throwable ->
            if (throwable is RuStoreReviewExistsException) reviewAlreadyExists = true
            Log.w(TAG, "requestReviewFlow", throwable)
        }
    }

    fun launchReviewFlow() {
        Log.d(TAG, "Review flow launched.")
        val reviewInfo = reviewInfo
        if (reviewInfo != null) {
            reviewManager.launchReviewFlow(reviewInfo).addOnSuccessListener {
                _event.tryEmit(UserFlowEvent.ReviewEnd)
                Log.d(TAG, "Review flow completed successfully.")
            }.addOnFailureListener { throwable ->
                _event.tryEmit(UserFlowEvent.ReviewEnd)
                Log.w(TAG, "launchReviewFlow", throwable)
            }
        } else {
            Log.w(TAG, "Review info is null.")
            _event.tryEmit(if (reviewAlreadyExists) UserFlowEvent.ReviewExists else UserFlowEvent.ReviewEnd)
        }
    }

    companion object {
        private const val TAG = "RuStoreReview"
    }
}

sealed interface UserFlowEvent {
    data object ReviewEnd : UserFlowEvent
    data object ReviewExists : UserFlowEvent
}