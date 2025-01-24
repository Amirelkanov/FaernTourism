package com.example.faerntourism.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface GoogleAuthRepository {
    val currentUser: FirebaseUser?
    suspend fun signInWithToken(googleIdToken: String): Result<FirebaseUser>
    suspend fun signOut(clearCredentialState: suspend () -> Unit): Result<Unit>
}

class GoogleAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : GoogleAuthRepository {
    override val currentUser: FirebaseUser? = firebaseAuth.currentUser

    override suspend fun signInWithToken(googleIdToken: String): Result<FirebaseUser> {
        return try {
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            val result = firebaseAuth.signInWithCredential(firebaseCredential).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun signOut(clearCredentialState: suspend () -> Unit): Result<Unit> {
        return try {
            clearCredentialState()
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}