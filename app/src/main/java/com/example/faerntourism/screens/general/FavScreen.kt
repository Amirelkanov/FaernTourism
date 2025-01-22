package com.example.faerntourism.screens.general

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.faerntourism.ui.theme.FaernTourismTheme

// TODO: Only for logged-in users
@Composable
fun FavScreen(
    modifier: Modifier = Modifier,
) {
    FaernTourismTheme {
        Scaffold() { padding ->
            Text("There will be favourites", Modifier.padding(padding))
        }
    }
}