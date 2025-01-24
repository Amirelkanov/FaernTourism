package com.example.faerntourism.ui.screens.general

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.faerntourism.Account
import com.example.faerntourism.FaernDestination
import com.example.faerntourism.R
import com.example.faerntourism.ui.AuthViewModel
import com.example.faerntourism.ui.components.GeneralScreenWrapper
import com.example.faerntourism.ui.theme.FaernTourismTheme
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch


@Composable
fun AccountScreen(
    onBottomTabSelected: (FaernDestination) -> Unit,
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
) {
    val authResource = viewModel.loginFlow.collectAsState()

    val context = LocalContext.current
    val credentialManager: CredentialManager = CredentialManager.create(context)
    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(buildGoogleIdOption(context))
        .build()

    val scope = rememberCoroutineScope()

    FaernTourismTheme {
        GeneralScreenWrapper(
            currentScreen = Account,
            onBottomTabSelected = onBottomTabSelected,
            content = {
                Column {
                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    val credentialResult = credentialManager.getCredential(
                                        request = request,
                                        context = context
                                    )

                                    val googleIdTokenCredential =
                                        GoogleIdTokenCredential.createFrom(
                                            credentialResult.credential.data
                                        )
                                    val googleIdToken = googleIdTokenCredential.idToken

                                    viewModel.signIn(googleIdToken)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    ) {
                        Text("Sign in")
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.signOut {
                                    credentialManager.clearCredentialState(
                                        ClearCredentialStateRequest()
                                    )
                                }
                            }
                        }
                    ) {
                        Text("Sign out")
                    }

                    authResource.value?.let { result ->
                        result.onSuccess { user ->
                            Text("Signed in as: ${user.displayName ?: "Unknown"}")
                        }.onFailure { e ->
                            Text("Error: ${e.message}")
                        }
                    }
                }
            },
            modifier = modifier.padding(horizontal = 10.dp)
        )
    }
}

private fun buildGoogleIdOption(context: Context): GetGoogleIdOption {
    return GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId(context.getString(R.string.web_client_id))
        .build()
}
