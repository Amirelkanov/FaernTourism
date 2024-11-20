package com.example.faerntourism.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.faerntourism.models.UserData

@Composable
fun HomeScreen(
    userData: UserData?,
    onSignInClick: () -> Unit,
    onSignOut: () -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (userData?.username != null) {
            Text(
                text = userData.username,
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (userData !== null) {
                Button(onClick = onSignOut) {
                    Text(text = "Sign out")
                }
            } else {
                Button(onClick = onSignInClick) {
                    Text(text = "Sign in")
                }
            }
        }

    }
}


