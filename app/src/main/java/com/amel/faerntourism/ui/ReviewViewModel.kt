package com.amel.faerntourism.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.rustore.sdk.review.RuStoreReviewManager
import ru.rustore.sdk.review.RuStoreReviewManagerFactory
import ru.rustore.sdk.review.model.ReviewInfo
import ru.rustore.sdk.review.errors.RuStoreReviewExists as RuStoreReviewExistsException

class ReviewViewModel : ViewModel() {

    private var isInitCalled: Boolean = false

    private val _event = MutableSharedFlow<UserFlowEvent>(
        extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event = _event.asSharedFlow()


    private lateinit var reviewManager: RuStoreReviewManager

    private var reviewInfo: ReviewInfo? = null
    private var reviewAlreadyExists = false

    fun init(context: Context) {
        if (isInitCalled) return

        createReviewManager(context)
        requestReviewFlow()

        isInitCalled = true
    }

    private fun createReviewManager(context: Context) {
        reviewManager = RuStoreReviewManagerFactory.create(context)
    }

    private fun requestReviewFlow() {
        if (reviewInfo != null) return
        reviewManager.requestReviewFlow().addOnSuccessListener { reviewInfo ->
            this.reviewInfo = reviewInfo
            Log.d(TAG, "Successfully requested review info.")
        }.addOnFailureListener { throwable ->
            if (throwable is RuStoreReviewExistsException) reviewAlreadyExists = true
            Log.e(TAG, throwable.toString())
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
                Log.e(TAG, throwable.toString())
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