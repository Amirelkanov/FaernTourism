package com.example.faerntourism.data

import android.util.Log
import com.example.faerntourism.data.model.Place
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


interface FireStoreRepository {
    suspend fun getPlaces(): Result<List<Place>>
}

class FireStoreRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : FireStoreRepository {
    override suspend fun getPlaces(): Result<List<Place>> {
        return try {
            val snapshot = fireStore.collection("places").get().await()
            val places = snapshot.toObjects<Place>()
            Result.success(places)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}