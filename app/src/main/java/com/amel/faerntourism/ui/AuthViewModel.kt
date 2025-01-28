package com.amel.faerntourism.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amel.faerntourism.data.FirebaseAuthRepository
import com.amel.faerntourism.data.model.UserData
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: FirebaseAuthRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<Result<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Result<FirebaseUser>?> = _loginFlow

    init {
        if (repository.currentUser != null) {
            _loginFlow.value = Result.success(repository.currentUser!!)
        }
    }

    fun signIn(googleIdToken: String) = viewModelScope.launch {
        val result = repository.signInWithToken(googleIdToken)
        _loginFlow.value = result
    }

    fun signOut(clearCredentialState: suspend () -> Unit = {}) = viewModelScope.launch {
        repository.signOut(clearCredentialState)
        _loginFlow.value = null
    }

    companion object {
        fun FirebaseUser.toUserData() =
            UserData(
                userId = uid,
                username = displayName,
                email = email,
                profilePictureUrl = photoUrl?.toString()
            )
    }
}