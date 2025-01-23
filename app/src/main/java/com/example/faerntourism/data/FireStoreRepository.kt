package com.example.faerntourism.data

import com.example.faerntourism.data.model.Article
import com.example.faerntourism.data.model.Place
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


interface FireStoreRepository {
    suspend fun getPlaces(): Result<List<Place>>
    suspend fun getArticles(): Result<List<Article>>

    suspend fun getPlace(id: String): Result<Place>
    suspend fun getArticle(id: String): Result<Article>
}

class FireStoreRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : FireStoreRepository {
    override suspend fun getPlaces(): Result<List<Place>> =
        try {
            val snapshot = fireStore.collection("places").get().await()

            val places = snapshot.documents.map { document ->
                document.toObject<Place>()?.copy(id = document.id) ?: Place()
            }

            Result.success(places)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getArticles(): Result<List<Article>> =
        try {
            val snapshot = fireStore.collection("articles").get().await()

            val articles = snapshot.documents.map { document ->
                document.toObject<Article>()!!.copy(id = document.id)
            }

            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }


    override suspend fun getPlace(id: String): Result<Place> =
        try {
            val docSnapshot = fireStore.collection("places")
                .document(id)
                .get()
                .await()

            if (docSnapshot.exists()) {
                Result.success(
                    docSnapshot.toObject<Place>()!!.copy(id = docSnapshot.id)
                )
            } else {
                Result.failure(NoSuchElementException("No place found with id $id"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }


    override suspend fun getArticle(id: String): Result<Article> =
        try {
            val docSnapshot = fireStore.collection("articles")
                .document(id)
                .get()
                .await()

            if (docSnapshot.exists()) {
                Result.success(
                    docSnapshot.toObject<Article>()!!.copy(id = docSnapshot.id)
                )
            } else {
                Result.failure(NoSuchElementException("No article found with id $id"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
}