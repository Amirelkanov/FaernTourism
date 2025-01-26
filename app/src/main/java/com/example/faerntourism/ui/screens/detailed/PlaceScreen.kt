package com.example.faerntourism.ui.screens.detailed

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.faerntourism.data.model.Place
import com.example.faerntourism.ui.components.FaernCard
import com.example.faerntourism.ui.components.FaernMap
import com.example.faerntourism.ui.components.Section
import com.example.faerntourism.ui.screens.general.PlaceViewState
import com.example.faerntourism.ui.screens.general.PlacesViewModel
import com.example.faerntourism.ui.screens.side.ErrorScreen
import com.example.faerntourism.ui.screens.side.LoadingScreen
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun PlaceScreen(
    placeId: String,
    navigateBack: () -> Unit,
    placesViewModel: PlacesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        placesViewModel.getPlace(placeId)
    }

    val placeViewState by placesViewModel.placeViewStateFlow.collectAsState()

    when (val state = placeViewState) {
        is PlaceViewState.Success -> SinglePlaceInfo(
            state.place, navigateBack, modifier
        )

        is PlaceViewState.Error -> {
            ErrorScreen(
                retryAction = { placesViewModel.getPlace(placeId) },
                modifier = modifier.fillMaxSize()
            )
        }

        else -> LoadingScreen(modifier = modifier.fillMaxSize())
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SinglePlaceInfo(
    place: Place, navigateBack: () -> Unit, modifier: Modifier = Modifier
) {

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val titles = listOf("Информация", "Расположение")

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val uriHandler = LocalUriHandler.current

    Scaffold(topBar = {
        FaernCard(
            mainTitle = place.name,
            secondaryTitle = "500 м", // TODO
            photoURL = place.imgLink,
            navigateBack = navigateBack
        )
    }) { contentPadding ->
        Column(modifier.padding(contentPadding)) {
            PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                titles.forEachIndexed { index, title ->
                    Tab(selected = selectedTabIndex == index, onClick = {
                        coroutineScope.launch {
                            selectedTabIndex = index
                        }
                    }, text = {
                        Text(
                            text = title, maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                    })
                }
            }

            when (selectedTabIndex) {
                0 -> Section(
                    title = titles[0], information = {
                        if (place.description.isEmpty()) Text(text = "Описание отсутствует.")
                        else Text(
                            text = place.description,
                            style = TextStyle(
                                color = colorScheme.onSurface,
                                fontSize = 18.sp
                            ),
                        )
                    }, contentPadding = PaddingValues(16.dp)
                )

                1 -> if (place.location != null) {
                    Section(
                        title = titles[1],
                        information = {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = fetchAddress(
                                        place.location.latitude,
                                        place.location.longitude,
                                        context
                                    ),
                                    style = TextStyle(
                                        color = colorScheme.onSurface,
                                        fontSize = 18.sp
                                    ),
                                )
                                FaernMap(
                                    place.location.latitude, place.location.longitude
                                )
                            }

                        },
                        actionButton = {
                            val yandexMapsUrl =
                                "https://maps.yandex.ru/?text=${place.location.latitude}+${place.location.longitude}"

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = { uriHandler.openUri(yandexMapsUrl) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Map,
                                        tint = colorScheme.onSurface,
                                        contentDescription = "Открыть карту"
                                    )
                                }
                                IconButton(
                                    onClick = { Log.d("yo", "todo") },
                                    modifier = Modifier.size(32.dp)
                                ) { // TODO
                                    Icon(
                                        imageVector = Icons.Default.Route,
                                        tint = colorScheme.onSurface,
                                        contentDescription = "Построить маршрут"
                                    )
                                }
                            }
                        },

                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    Section(
                        title = titles[1],
                        information = {
                            Text(
                                "Информации о местоположении отсутствует.",
                                style = TextStyle(
                                    color = colorScheme.onSurface, fontSize = 18.sp
                                ),
                            )
                        },
                        contentPadding = PaddingValues(16.dp)
                    )
                }
            }
        }
    }
}


private fun fetchAddress(latitude: Double, longitude: Double, context: Context): String {
    val geocoder = Geocoder(context, Locale("ru"))

    return try {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (!addresses.isNullOrEmpty()) {
            addresses[0].getAddressLine(0) ?: "No address found"
        } else {
            "No address found"
        }
    } catch (e: Exception) {
        "Error fetching address: ${e.message}"
    }
}
