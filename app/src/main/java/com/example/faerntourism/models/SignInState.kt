package com.example.faerntourism.models

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)