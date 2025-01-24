package com.example.faerntourism.ui.screens.general


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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.faerntourism.FaernDestination
import com.example.faerntourism.Home
import com.example.faerntourism.data.model.Place
import com.example.faerntourism.ui.components.FaernListItem
import com.example.faerntourism.ui.components.GeneralScreenWrapper
import com.example.faerntourism.ui.components.ListItemAdditionalInfo
import com.example.faerntourism.ui.components.SearchBar
import com.example.faerntourism.ui.screens.side.ErrorScreen
import com.example.faerntourism.ui.screens.side.LoadingScreen

@Composable
fun HomeScreen(
    onBottomTabSelected: (FaernDestination) -> Unit,
    onPlaceClick: (String) -> Unit,
    placesViewModel: PlacesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {

    LaunchedEffect(Unit) {
        placesViewModel.getPlacesData()
    }

    val placesViewState by placesViewModel.placeListViewStateFlow.collectAsState()

    GeneralScreenWrapper(
        currentScreen = Home,
        onBottomTabSelected = onBottomTabSelected,
        content = {
            when (val state = placesViewState) {
                is PlaceListViewState.Success -> PlacesFeedScreen(
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
            items(places.filter {
                it.name.contains(searchedText, ignoreCase = true)
            }) { place ->
                FaernListItem(
                    title = place.name,
                    description = place.description,
                    photoURL = place.imgLink,
                    descriptionMaxLines = 2,
                    additionalInfo = {
                        ListItemAdditionalInfo(
                            icon = {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = colorScheme.secondary
                                )
                            },
                            text = "50 м от вас"
                        )
                    },
                    modifier = Modifier.clickable(
                        onClick = { onPlaceClick(place.id) }
                    )
                )
            }
        }
    }
}
