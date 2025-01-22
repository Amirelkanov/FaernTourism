package com.example.faerntourism.ui.components

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.faerntourism.FaernDestination
import com.example.faerntourism.ui.theme.onPrimaryContainerLight
import com.example.faerntourism.ui.theme.onSurfaceLight
import com.example.faerntourism.ui.theme.secondaryContainerLight
import com.example.faerntourism.ui.theme.surfaceContainerLight


@Composable
fun FaernBottomNavigation(
    allScreens: List<FaernDestination>,
    currentScreen: FaernDestination,
    onBottomTabSelected: (FaernDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        containerColor = surfaceContainerLight,
        contentColor = onPrimaryContainerLight,
        modifier = modifier,
    ) {
        allScreens.forEach { screen ->
            val selected = currentScreen.route == screen.route
            NavigationBarItem(
                selected = selected,
                onClick = { if (!selected) onBottomTabSelected(screen) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = secondaryContainerLight,
                    selectedIconColor = onSurfaceLight,
                    selectedTextColor = onSurfaceLight,
                    unselectedIconColor = onPrimaryContainerLight,
                    unselectedTextColor = onPrimaryContainerLight
                ),
                label = {
                    Text(
                        text = stringResource(screen.titleResource),
                        modifier = Modifier.offset(y = (-2).dp)
                    )
                },
                icon = {
                    Icon(
                        imageVector = if (selected) {
                            screen.activeIcon
                        } else screen.inactiveIcon, contentDescription = null
                    )
                })
        }
    }
}
