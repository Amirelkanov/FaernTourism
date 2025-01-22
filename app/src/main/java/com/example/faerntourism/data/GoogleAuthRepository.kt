package com.example.faerntourism.data

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.faerntourism.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface GoogleAuthRepository {
    val currentUser: FirebaseUser?
    suspend fun signIn(): Result<FirebaseUser>
    suspend fun signOut(): Result<Unit>
}

class GoogleAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context,
) : GoogleAuthRepository {
    private val credentialManager: CredentialManager = CredentialManager.create(context)

    override val currentUser: FirebaseUser? = firebaseAuth.currentUser

    override suspend fun signIn(): Result<FirebaseUser> {
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(buildGoogleIdOption())
            .build()
        return try {
            val credential =
                credentialManager.getCredential(request = request, context = context).credential
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val googleIdToken = googleIdTokenCredential.idToken
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

            val result = firebaseAuth.signInWithCredential(firebaseCredential).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    private fun buildGoogleIdOption(): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(context.getString(R.string.web_client_id))
            .build()
    }
}