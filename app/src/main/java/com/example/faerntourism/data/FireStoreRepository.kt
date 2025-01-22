package com.example.faerntourism.data

import com.example.faerntourism.data.model.Article
import com.example.faerntourism.data.model.Place
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


interface FireStoreRepository {
    suspend fun getPlaces(): Result<List<Place>>
    suspend fun getArticles(): Result<List<Article>>
}

class FireStoreRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : FireStoreRepository {
    override suspend fun getPlaces(): Result<List<Place>> {
        return try {
            Result.success(
                fireStore.collection("places").get().await().toObjects<Place>()
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getArticles(): Result<List<Article>> {
        return try {
            Result.success(
                fireStore.collection("articles").get().await().toObjects<Article>()
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}