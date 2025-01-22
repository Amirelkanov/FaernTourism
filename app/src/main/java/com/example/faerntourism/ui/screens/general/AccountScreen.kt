package com.example.faerntourism.ui.screens.general

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.faerntourism.Account
import com.example.faerntourism.FaernDestination
import com.example.faerntourism.ui.AuthViewModel
import com.example.faerntourism.ui.components.GeneralScreenWrapper
import com.example.faerntourism.ui.theme.FaernTourismTheme


@Composable
fun AccountScreen(
    onBottomTabSelected: (FaernDestination) -> Unit,
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
) {
    val authResource = viewModel.loginFlow.collectAsState()

    FaernTourismTheme {
        GeneralScreenWrapper(
            currentScreen = Account,
            onBottomTabSelected = onBottomTabSelected,
            content = {
                Column {
                    Button(onClick = { viewModel.loginUser() }) {
                        Text("sign in")
                    }
                    Button(onClick = { viewModel.logout() }) {
                        Text("sign out")
                    }

                    authResource.value?.let {
                        it.onSuccess { user ->
                            user.displayName?.let { it1 -> Text(it1) }
                        }.onFailure { e ->
                            e.message?.let { it1 -> Text(it1) }
                        }
                    }

                }
            },
            modifier = modifier.padding(horizontal = 10.dp)
        )
    }
}