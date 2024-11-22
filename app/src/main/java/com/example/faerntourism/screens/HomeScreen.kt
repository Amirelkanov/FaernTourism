package com.example.faerntourism.screens


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.faerntourism.FAV_SCREEN
import com.example.faerntourism.HOME_SCREEN
import com.example.faerntourism.PLACE_ID
import com.example.faerntourism.PLACE_SCREEN
import com.example.faerntourism.R
import com.example.faerntourism.TOURS_SCREEN
import com.example.faerntourism.models.UserData
import com.example.faerntourism.ui.theme.FaernTourismTheme

// TODO: Поменять поведение скролла. Итемс залезают под боттомэппбар
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    userData: UserData? = null,
    onSignInClick: () -> Unit = {},
    onSignOut: () -> Unit = {},
    openScreen: (String) -> Unit = {},
) {

    Spacer(Modifier.height(16.dp))
    LazyColumn(
        Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            SearchBar(
                userData = userData, onSignInClick = onSignInClick, onSignOut = onSignOut
            )
        }
        itemsIndexed(placesData) { index, item ->
            PlaceCard(item.drawable, item.text,
                Modifier
                    .height(80.dp)
                    .clickable(
                        // TODO: вместо индекса надо будет айдишник из бд пихать
                        onClick = { openScreen("$PLACE_SCREEN?$PLACE_ID=$index") }
                    ))
        }
    }
    Spacer(Modifier.height(16.dp))
}

// TODO: подпраавь это говно
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    userData: UserData? = null,
    onSignInClick: () -> Unit = {},
    onSignOut: () -> Unit = {},
) {

    OutlinedTextField(value = "", onValueChange = {}, leadingIcon = {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null
        )
    }, colors = TextFieldDefaults.colors(
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        focusedContainerColor = MaterialTheme.colorScheme.surface
    ), placeholder = {
        Text(stringResource(R.string.placeholder_search))
    }, shape = MaterialTheme.shapes.extraLarge, trailingIcon = {
        // TODO: сделать не просто кнопка и тд, а выплывающее окошко с аккаунтом и сменить аккаунт типа
        if (userData !== null) {
            IconButton(
                onClick = onSignOut
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
            }
        } else {
            IconButton(
                onClick = onSignInClick
            ) {
                Icon(Icons.Default.AccountCircle, contentDescription = null)
            }
        }

    },
        modifier = modifier
            .heightIn(min = 56.dp) // minHeight типа
            .fillMaxWidth()
    )
}


// todo: поведение с selected
// todo: обобщить для всех окон
@Composable
fun FaernBottomNavigation(modifier: Modifier = Modifier, openScreen: (String) -> Unit = {}) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant, modifier = modifier
    ) {
        NavigationBarItem(icon = {
            Icon(
                imageVector = Icons.Default.Favorite, contentDescription = null
            )
        }, label = {
            Text(stringResource(R.string.bottom_navigation_favourite))
        }, selected = false, onClick = { openScreen(FAV_SCREEN) })
        NavigationBarItem(icon = {
            Icon(
                imageVector = Icons.Default.Home, contentDescription = null
            )
        }, label = {
            Text(stringResource(R.string.bottom_navigation_home))
        }, selected = false, onClick = { openScreen(HOME_SCREEN) })
        NavigationBarItem(icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.MenuBook, contentDescription = null
            )
        }, label = {
            Text(stringResource(R.string.bottom_navigation_tours))
        }, selected = false, onClick = { openScreen(TOURS_SCREEN) })
    }
}


@Composable
fun PlaceCard(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(drawable),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = stringResource(text),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Lorem ipsum odor amet, consectetuer adipiscing elit. Duis turpis cras congue vehicula fames sodales elit risus. Molestie ut facilisi quisque dui est nisi porta. Felis enim venenatis hendrerit rutrum praesent curabitur magnis",
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 2
                )
                GeoTagPlaceInfo(50)
            }
        }
    }
}

@Composable
fun GeoTagPlaceInfo(
    distance: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.LocationOn,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = "$distance м. от вас",
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
fun FaernTourismAppPortrait(
    userData: UserData? = null,
    onSignInClick: () -> Unit = {},
    onSignOut: () -> Unit = {},
    openScreen: (String) -> Unit = {},
) {
    // TODO: topbar
    FaernTourismTheme {
        Scaffold(bottomBar = { FaernBottomNavigation(openScreen = openScreen) }) { padding ->
            HomeScreen(Modifier.padding(padding), userData, onSignInClick, onSignOut, openScreen)
        }
    }
}

private val placesData = listOf(
    R.drawable.philharmonic to R.string.philarmonic,
    R.drawable.philharmonic to R.string.philarmonic,
    R.drawable.philharmonic to R.string.philarmonic,
    R.drawable.philharmonic to R.string.philarmonic,
    R.drawable.philharmonic to R.string.philarmonic,
    R.drawable.philharmonic to R.string.philarmonic,
    R.drawable.philharmonic to R.string.philarmonic,
    R.drawable.philharmonic to R.string.philarmonic,
    R.drawable.philharmonic to R.string.philarmonic,
    R.drawable.philharmonic to R.string.philarmonic,
).map { DrawableStringPair(it.first, it.second) }

private data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    @StringRes val text: Int,
)

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun BottomNavigationPreview() {
    FaernTourismTheme { FaernBottomNavigation(Modifier.padding(top = 24.dp)) }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun SearchBarPreview() {
    FaernTourismTheme {
        SearchBar(Modifier.padding(8.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun PlaceCardPreview() {
    FaernTourismTheme {
        PlaceCard(
            drawable = R.drawable.philharmonic,
            text = R.string.philarmonic,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun GeoTagPlaceInfoPreview() {
    FaernTourismTheme {
        GeoTagPlaceInfo(50) // TODO: дистанция должна рассчитываться
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun HomeScreenPreview() {
    FaernTourismTheme {
        HomeScreen()
    }
}

