package com.example.faerntourism.ui.screens.general


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.faerntourism.FaernDestination
import com.example.faerntourism.Home
import com.example.faerntourism.R
import com.example.faerntourism.data.model.Place
import com.example.faerntourism.ui.PlacesViewState
import com.example.faerntourism.ui.components.GeneralScreenWrapper
import com.example.faerntourism.ui.components.ListItemAdditionalInfo
import com.example.faerntourism.ui.components.MyListItem
import com.example.faerntourism.ui.components.SearchBar
import com.example.faerntourism.ui.theme.secondaryLight

@Composable
fun HomeScreen(
    onBottomTabSelected: (FaernDestination) -> Unit,
    onPlaceClick: (String) -> Unit,
    placesViewState: PlacesViewState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {

    GeneralScreenWrapper(
        currentScreen = Home,
        onBottomTabSelected = onBottomTabSelected,
        content = {
            when (placesViewState) {
                is PlacesViewState.Success -> PlacesFeedScreen(
                    placesViewState.places,
                    onPlaceClick,
                    modifier
                )

                is PlacesViewState.Error -> {
                    Log.d("myErr", placesViewState.errorMsg)
                    ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
                }

                else -> LoadingScreen(modifier = modifier.fillMaxSize())
            }
        },
        modifier = Modifier.padding(horizontal = 10.dp)
    )
}

@Composable
fun PlacesFeedScreen(
    places: List<Place>,
    onPlaceClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {
        val textState = remember {
            mutableStateOf(TextFieldValue(""))
        }

        SearchBar(state = textState)

        val searchedText = textState.value.text

        LazyColumn {
            itemsIndexed(places.filter {
                it.name.contains(searchedText, ignoreCase = true)
            }) { index, place ->
                MyListItem(
                    place.name, place.description, 2, null,
                    additionalInfo = {
                        ListItemAdditionalInfo(
                            icon = {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = secondaryLight
                                )
                            },
                            text = "50 м от вас"
                        )
                    },
                    modifier = Modifier.clickable(
                        onClick = { onPlaceClick(index.toString()) }
                    )
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}
