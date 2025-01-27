package com.amel.faerntourism.ui.screens.general


import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amel.faerntourism.FaernDestination
import com.amel.faerntourism.Home
import com.amel.faerntourism.data.model.Place
import com.amel.faerntourism.ui.LocationViewModel
import com.amel.faerntourism.ui.LocationViewModel.Companion.prettifyDistance
import com.amel.faerntourism.ui.LocationViewModel.Companion.toLocation
import com.amel.faerntourism.ui.components.FaernListItem
import com.amel.faerntourism.ui.components.GeneralScreenWrapper
import com.amel.faerntourism.ui.components.ListItemAdditionalInfo
import com.amel.faerntourism.ui.components.SearchBar
import com.amel.faerntourism.ui.screens.side.ErrorScreen
import com.amel.faerntourism.ui.screens.side.LoadingScreen

@Composable
fun HomeScreen(
    onBottomTabSelected: (FaernDestination) -> Unit,
    onPlaceClick: (String) -> Unit,
    locationViewModel: LocationViewModel = hiltViewModel(),
    placesViewModel: PlacesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {

    LaunchedEffect(Unit) {
        placesViewModel.getPlacesData()
    }

    val placesViewState by placesViewModel.placeListViewStateFlow.collectAsState()
    val location by locationViewModel.locationState.collectAsState()

    LaunchedEffect(Unit) {
        locationViewModel.startTracking()
    }

    // Clean up when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            locationViewModel.stopTracking()
        }
    }

    GeneralScreenWrapper(
        currentScreen = Home,
        onBottomTabSelected = onBottomTabSelected,
        content = {
            when (val state = placesViewState) {
                is PlaceListViewState.Success -> PlacesFeedScreen(
                    location,
                    state.places,
                    onPlaceClick,
                    modifier
                )

                is PlaceListViewState.Error -> ErrorScreen(
                    placesViewModel::getPlacesData,
                    modifier = modifier.fillMaxSize()
                )

                else -> LoadingScreen(modifier = modifier.fillMaxSize())
            }
        },
        modifier = Modifier.padding(horizontal = 10.dp)
    )
}

@Composable
fun PlacesFeedScreen(
    userLocation: Location?,
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

        val sortedPlaces = remember(userLocation, places, searchedText) {
            places.filter {
                it.name.contains(searchedText, ignoreCase = true) && it.location != null
            }.sortedBy { place ->
                userLocation?.distanceTo(Location("").apply {
                    latitude = place.location!!.latitude
                    longitude = place.location.longitude
                }) ?: Float.MAX_VALUE
            }
        }


        LazyColumn {
            items(sortedPlaces) { place ->
                FaernListItem(
                    title = place.name,
                    description = place.description,
                    photoURL = place.imgLink,
                    descriptionMaxLines = 2,
                    additionalInfo = {
                        if (place.location != null)
                            ListItemAdditionalInfo(
                                icon = {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = colorScheme.secondary
                                    )
                                },
                                text = if (userLocation != null)
                                    "${prettifyDistance(userLocation.distanceTo(place.location.toLocation()))} от вас"
                                else "Загрузка..."
                            )
                        else Text("")
                    },
                    modifier = Modifier.clickable(
                        onClick = { onPlaceClick(place.id) }
                    )
                )
            }
        }
    }
}



