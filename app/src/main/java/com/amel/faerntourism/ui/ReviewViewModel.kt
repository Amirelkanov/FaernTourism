package com.amel.faerntourism.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.rustore.sdk.review.RuStoreReviewManager
import ru.rustore.sdk.review.model.ReviewInfo
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewManager: RuStoreReviewManager
) : ViewModel() {
    private var reviewInfo: ReviewInfo? = null

    init {
        requestReviewFlow()
    }

    private fun requestReviewFlow() {
        reviewManager.requestReviewFlow()
            .addOnSuccessListener { info ->
                reviewInfo = info
                Log.d(TAG, "Successfully requested review info.")
            }
            .addOnFailureListener { throwable ->
                Log.e(
                    TAG, "Failed to request review info: ${throwable.message}",
                    throwable
                )
            }
    }

    fun launchReviewFlow() {
        val info = reviewInfo
        if (info == null) {
            Log.e(
                TAG,
                "ReviewInfo is null. Possibly requestReviewFlow() failed or hasn't returned yet."
            )
            return
        }
        reviewManager.launchReviewFlow(info)
            .addOnSuccessListener {
                Log.d(TAG, "Review flow completed successfully.")
            }
            .addOnFailureListener { throwable ->
                Log.e(
                    TAG, "Failed to launch review flow: ${throwable.message}",
                    throwable
                )
            }
    }

    companion object {
        private const val TAG = "RuStoreReview"
    }
}
