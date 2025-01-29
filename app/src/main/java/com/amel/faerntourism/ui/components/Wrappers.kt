package com.amel.faerntourism.ui.components

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amel.faerntourism.FaernDestination
import com.amel.faerntourism.faernBottomNavigationBarScreens
import com.amel.faerntourism.ui.UpdateEvent
import com.amel.faerntourism.ui.UpdateViewModel
import kotlinx.coroutines.launch


@Composable
fun GeneralScreenWrapper(
    currentScreen: FaernDestination,
    onBottomTabSelected: (FaernDestination) -> Unit,
    content: @Composable () -> Unit,
    updateViewModel: UpdateViewModel,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(updateViewModel) {
        updateViewModel.events.collect { event ->
            when (event) {
                is UpdateEvent.UpdateDownloaded -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Установить скачанное обновление?",
                            actionLabel = "Установить",
                            duration = SnackbarDuration.Long
                        ).also { result ->
                            if (result == SnackbarResult.ActionPerformed) {
                                updateViewModel.completeUpdateRequested()
                            }
                        }
                    }
                }

                is UpdateEvent.UpdateDenied -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Обновление невозможно. Разрешите установку из сторонних источников в настройках RuStore.",
                            actionLabel = "Перейти",
                            duration = SnackbarDuration.Long
                        ).also { result ->
                            if (result == SnackbarResult.ActionPerformed) {
                                val intent =
                                    Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                                        data = Uri.parse("package:ru.vk.store")
                                    }
                                context.startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
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
fun Section(
    title: String,
    information: @Composable () -> Unit,
    actionButton: (@Composable () -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    title,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorScheme.onSurface
                )
                actionButton?.invoke()
            }
        }
        item {
            information()
        }
    }
}
