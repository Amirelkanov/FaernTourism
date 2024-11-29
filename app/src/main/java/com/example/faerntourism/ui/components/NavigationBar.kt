package com.example.faerntourism.ui.components

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.faerntourism.ACCOUNT_SCREEN
import com.example.faerntourism.CULTURE_SCREEN
import com.example.faerntourism.HOME_SCREEN
import com.example.faerntourism.R
import com.example.faerntourism.TOURS_SCREEN
import com.example.faerntourism.ui.theme.FaernTourismTheme
import com.example.faerntourism.ui.theme.onPrimaryContainerLight
import com.example.faerntourism.ui.theme.onSurfaceLight
import com.example.faerntourism.ui.theme.secondaryContainerLight
import com.example.faerntourism.ui.theme.surfaceContainerLight


data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val navigateScreen: String
)

@Composable
fun FaernBottomNavigation(modifier: Modifier = Modifier, openScreen: (String) -> Unit = {}) {
    // TODO: разберись
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        containerColor = surfaceContainerLight,
        contentColor = onPrimaryContainerLight,
        modifier = modifier,
    ) {
        navBarItems().forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    openScreen(item.navigateScreen)
                },
                colors = NavigationBarItemDefaults
                    .colors(
                        indicatorColor = secondaryContainerLight,
                        selectedIconColor = onSurfaceLight,
                        selectedTextColor = onSurfaceLight,
                        unselectedIconColor = onPrimaryContainerLight,
                        unselectedTextColor = onPrimaryContainerLight
                    ),
                label = {
                    Text(text = item.title, modifier = Modifier.offset(y = (-2).dp))
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedItemIndex) {
                            item.selectedIcon
                        } else item.unselectedIcon,
                        contentDescription = item.title
                    )
                }
            )
        }
    }
}

@Composable
private fun navBarItems() = listOf(
    BottomNavigationItem(
        title = stringResource(R.string.bottom_navigation_home),
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        navigateScreen = HOME_SCREEN
    ),
    BottomNavigationItem(
        title = stringResource(R.string.bottom_navigation_tours),
        selectedIcon = Icons.Filled.Landscape,
        unselectedIcon = Icons.Outlined.Landscape,
        navigateScreen = TOURS_SCREEN
    ),
    BottomNavigationItem(
        title = stringResource(R.string.bottom_navigation_culture),
        selectedIcon = ImageVector.vectorResource(R.drawable.menu_book_filled_24px),
        unselectedIcon = ImageVector.vectorResource(R.drawable.menu_book_outline_24px),
        navigateScreen = CULTURE_SCREEN
    ),
    BottomNavigationItem(
        title = stringResource(R.string.bottom_navigation_culture),
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        navigateScreen = ACCOUNT_SCREEN
    ),
)


@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun BottomNavigationPreview() {
    FaernTourismTheme { FaernBottomNavigation(Modifier.padding(top = 24.dp)) }
}
