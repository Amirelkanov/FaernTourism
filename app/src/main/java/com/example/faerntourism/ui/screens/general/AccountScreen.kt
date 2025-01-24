package com.example.faerntourism.ui.screens.general

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.faerntourism.Account
import com.example.faerntourism.FaernDestination
import com.example.faerntourism.R
import com.example.faerntourism.data.model.UserData
import com.example.faerntourism.ui.AuthViewModel
import com.example.faerntourism.ui.AuthViewModel.Companion.toUserData
import com.example.faerntourism.ui.components.GeneralScreenWrapper
import com.example.faerntourism.ui.theme.FaernTourismTheme
import com.example.faerntourism.ui.theme.onErrorContainerLight
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
    onBottomTabSelected: (FaernDestination) -> Unit,
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
) {
    val authResource by viewModel.loginFlow.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val credentialManager = CredentialManager.create(context)
    val request = GetCredentialRequest.Builder()
        .addCredentialOption(buildGoogleIdOption(context))
        .build()

    FaernTourismTheme {
        GeneralScreenWrapper(
            currentScreen = Account,
            onBottomTabSelected = onBottomTabSelected,
            content = {
                authResource?.let { result ->
                    result.onSuccess { currentUser ->
                        LoggedUserScreen(
                            userData = currentUser.toUserData(),
                            onLogoutClick = {
                                scope.launch {
                                    viewModel.signOut {
                                        credentialManager.clearCredentialState(
                                            ClearCredentialStateRequest()
                                        )
                                    }
                                }
                            }
                        )
                    }.onFailure { e ->
                        e.printStackTrace()
                    }
                } ?: NotLoggedUserScreen(
                    onLoginClick = {
                        scope.launch {
                            try {
                                val credentialResult =
                                    credentialManager.getCredential(context, request)
                                val googleIdTokenCredential =
                                    GoogleIdTokenCredential.createFrom(credentialResult.credential.data)
                                val googleIdToken = googleIdTokenCredential.idToken

                                viewModel.signIn(googleIdToken)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                )
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


@Composable
fun LoggedUserScreen(
    userData: UserData,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(userData.profilePictureUrl + "was")
                    .crossfade(true).build(),
                contentDescription = "Фото пользователя ${userData.username ?: ""}",
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.default_user_avatar),
                placeholder = painterResource(R.drawable.loading_img),
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
            )
            Text(userData.username ?: "Пользователь")
            Text(userData.email ?: "Почта")
        }

        Spacer(modifier = Modifier.height(30.dp))
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .clickable(onClick = onLogoutClick)
                    .fillMaxWidth()

            ) {
                Row {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                        tint = onErrorContainerLight,
                        contentDescription = null
                    )
                    Text("Выйти")
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                    tint = onErrorContainerLight,
                    contentDescription = null
                )
            }
        }
    }


}

@Composable
fun NotLoggedUserScreen(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Вы не авторизованы.", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onLoginClick) {
            Text(text = "Войти")
        }
    }
}
