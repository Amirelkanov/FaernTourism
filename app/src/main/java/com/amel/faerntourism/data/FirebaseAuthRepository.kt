package com.amel.faerntourism.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface FirebaseAuthRepository {
    val currentUser: FirebaseUser?
    suspend fun signInWithToken(googleIdToken: String): Result<FirebaseUser>
    suspend fun signOut(clearCredentialState: suspend () -> Unit): Result<Unit>
}

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : FirebaseAuthRepository {
    override val currentUser: FirebaseUser? = firebaseAuth.currentUser

    override suspend fun signInWithToken(googleIdToken: String): Result<FirebaseUser> {
        return try {
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            val result = firebaseAuth.signInWithCredential(firebaseCredential).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Log.w(TAG, e)
            Result.failure(e)
        }
    }

    override suspend fun signOut(clearCredentialState: suspend () -> Unit): Result<Unit> {
        return try {
            clearCredentialState()
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.w(TAG, e)
            Result.failure(e)
        }
    }

    companion object {
        const val TAG = "FirebaseAuthRepository"
    }
}