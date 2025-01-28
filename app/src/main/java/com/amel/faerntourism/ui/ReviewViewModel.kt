package com.amel.faerntourism.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.rustore.sdk.review.RuStoreReviewManager
import ru.rustore.sdk.review.RuStoreReviewManagerFactory
import ru.rustore.sdk.review.model.ReviewInfo
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val reviewManager: RuStoreReviewManager = RuStoreReviewManagerFactory.create(context)
    private var reviewInfo: ReviewInfo? = null

    init {
        // Запрашиваем reviewInfo в начале флоу пользователя,
        // где-то за 0-3 минуты до показа шторки,
        // чтобы запуск шторки оценки в конце флоу произошел мгновенно.
        requestReviewFlow()
    }

    private fun requestReviewFlow() {
        reviewManager.requestReviewFlow()
            .addOnSuccessListener { info ->
                reviewInfo = info
                Log.d("RuStoreReview", "Successfully requested review info.")
            }
            .addOnFailureListener { throwable ->
                Log.e(
                    "RuStoreReview",
                    "Failed to request review info: ${throwable.message}",
                    throwable
                )
            }
    }

    fun launchReviewFlow() {
        val info = reviewInfo
        if (info == null) {
            Log.e(
                "RuStoreReview",
                "ReviewInfo is null. Possibly requestReviewFlow() failed or hasn't returned yet."
            )
            return
        }
        reviewManager.launchReviewFlow(info)
            .addOnSuccessListener {
                Log.d("RuStoreReview", "Review flow completed successfully.")
            }
            .addOnFailureListener { throwable ->
                Log.e(
                    "RuStoreReview",
                    "Failed to launch review flow: ${throwable.message}",
                    throwable
                )
            }
    }
}
