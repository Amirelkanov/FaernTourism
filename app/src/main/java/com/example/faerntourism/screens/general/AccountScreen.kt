package com.example.faerntourism.screens.general

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.faerntourism.ui.components.FaernBottomNavigation
import com.example.faerntourism.ui.theme.FaernTourismTheme


@Composable
fun AccountScreen() {
    FaernTourismTheme {
        Scaffold() { padding ->
            Text("There will be account", Modifier.padding(padding))
        }
    }
}