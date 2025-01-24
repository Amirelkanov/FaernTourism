package com.example.faerntourism.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.faerntourism.FaernDestination
import com.example.faerntourism.faernBottomNavigationBarScreens


@Composable
fun GeneralScreenWrapper(
    currentScreen: FaernDestination,
    onBottomTabSelected: (FaernDestination) -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
                    .background(color = colorScheme.background)
            ) {
                Text(
                    currentScreen.topAppBarTitle,
                    fontSize = 39.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = modifier.padding(horizontal = 5.dp)
                )
            }
        },
        bottomBar = {
            FaernBottomNavigation(
                allScreens = faernBottomNavigationBarScreens,
                currentScreen = currentScreen,
                onBottomTabSelected = onBottomTabSelected
            )
        },
        containerColor = colorScheme.background
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}


@Composable
fun DetailedScreenWrapper(
    mainCardTitle: String,
    secondaryCardTitle: String,
    photoURL: String = "",
    navigateBack: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(topBar = {
        FaernCard(
            mainCardTitle,
            secondaryCardTitle,
            photoURL,
            navigateBack = navigateBack
        )
    }) { contentPadding ->
        Box(modifier = modifier.padding(contentPadding)) {
            content()
        }
    }
}

@Composable
fun Section(
    title: String,
    information: @Composable () -> Unit,
    actionButton: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium,
                color = colorScheme.onSurface
            )
            actionButton?.invoke()
        }
        information()
    }
}
