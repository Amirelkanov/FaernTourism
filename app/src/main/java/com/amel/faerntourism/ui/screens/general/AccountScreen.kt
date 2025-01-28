package com.amel.faerntourism.ui.screens.general

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.core.content.ContextCompat.getString
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amel.faerntourism.Account
import com.amel.faerntourism.FaernDestination
import com.amel.faerntourism.R
import com.amel.faerntourism.data.model.UserData
import com.amel.faerntourism.ui.AuthViewModel
import com.amel.faerntourism.ui.AuthViewModel.Companion.toUserData
import com.amel.faerntourism.ui.ReviewViewModel
import com.amel.faerntourism.ui.UserFlowEvent
import com.amel.faerntourism.ui.components.AccountSettingsListItem
import com.amel.faerntourism.ui.components.GeneralScreenWrapper
import com.amel.faerntourism.ui.theme.FaernTourismTheme
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
    onBottomTabSelected: (FaernDestination) -> Unit,
    authViewModel: AuthViewModel,
    reviewViewModel: ReviewViewModel,
    modifier: Modifier = Modifier,
) {
    val authResource by authViewModel.loginFlow.collectAsState()

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val oneTapClient = Identity.getSignInClient(context)
    val oneTapLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    scope.launch {
                        authViewModel.signIn(idToken)
                    }
                }
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }


    LaunchedEffect(Unit) {
        reviewViewModel.event.collect { event ->
            when (event) {
                is UserFlowEvent.ReviewEnd -> {}
                is UserFlowEvent.ReviewExists -> {
                    Toast.makeText(
                        context,
                        "Вы уже оставляли отзыв на это приложение. Спасибо!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


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
                                    authViewModel.signOut()
                                }
                            },
                            onLeaveReviewClick = {
                                reviewViewModel.launchReviewFlow()
                            }
                        )
                    }.onFailure { e ->
                        e.printStackTrace()
                    }
                } ?: NotLoggedUserScreen(
                    onLoginClick = {
                        scope.launch {
                            oneTapClient.beginSignIn(buildGoogleIdSignInRequest(context))
                                .addOnSuccessListener { result ->
                                    oneTapLauncher.launch(
                                        IntentSenderRequest.Builder(result.pendingIntent).build()
                                    )
                                }
                                .addOnFailureListener { e ->
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


@Composable
fun LoggedUserScreen(
    userData: UserData,
    onLogoutClick: () -> Unit,
    onLeaveReviewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(userData.profilePictureUrl)
                    .crossfade(true).build(),
                contentDescription = "Фото пользователя ${userData.username ?: ""}",
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.default_user_avatar),
                placeholder = painterResource(R.drawable.loading_img),
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                userData.username ?: "Пользователь",
                style = typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                userData.email ?: "Почта",
                style = typography.bodyLarge,
                color = colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            AccountSettingsListItem(
                leadingTitle = "Оценить в Rustore",
                leadingImgVector = Icons.Outlined.Star,
                onClick = onLeaveReviewClick
            )

            AccountSettingsListItem(
                leadingTitle = "Выйти",
                leadingImgVector = Icons.AutoMirrored.Outlined.Logout,
                onClick = onLogoutClick
            )
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Вы не авторизованы", style = typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onLoginClick,
            colors = ButtonColors(
                containerColor = colorScheme.onPrimaryContainer,
                contentColor = colorScheme.onPrimary,
                disabledContainerColor = colorScheme.background,
                disabledContentColor = colorScheme.secondary
            ),
            contentPadding = PaddingValues(20.dp, 15.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.google_icon),
                    contentDescription = null
                )

                Text(text = "Войти с Google", style = typography.titleLarge)
            }
        }
    }
}

private fun buildGoogleIdSignInRequest(context: Context): BeginSignInRequest {
    return BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(getString(context, R.string.web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()
}