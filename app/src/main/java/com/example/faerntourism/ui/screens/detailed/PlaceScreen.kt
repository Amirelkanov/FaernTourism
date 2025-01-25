package com.example.faerntourism.ui.screens.detailed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.faerntourism.data.model.Place
import com.example.faerntourism.ui.components.DetailedScreenWrapper
import com.example.faerntourism.ui.components.FaernMap
import com.example.faerntourism.ui.components.Section
import com.example.faerntourism.ui.screens.general.PlaceViewState
import com.example.faerntourism.ui.screens.general.PlacesViewModel
import com.example.faerntourism.ui.screens.side.ErrorScreen
import com.example.faerntourism.ui.screens.side.LoadingScreen
import kotlinx.coroutines.launch

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
    val listState = rememberLazyListState()

    val titles = listOf("Информация", "Расположение")
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }.collect { index ->
            if (index != selectedTabIndex) {
                selectedTabIndex = index
            }
        }
    }


    DetailedScreenWrapper(mainCardTitle = place.name,
        secondaryCardTitle = "500 м",
        photoURL = place.imgLink,
        navigateBack = {
            navigateBack()
        },
        content = {
            Column(modifier) {
                PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                    titles.forEachIndexed { index, title ->
                        Tab(selected = selectedTabIndex == index, onClick = {
                            coroutineScope.launch {
                                selectedTabIndex = index
                                listState.animateScrollToItem(index)
                            }
                        }, text = {
                            Text(
                                text = title, maxLines = 1, overflow = TextOverflow.Ellipsis
                            )
                        })
                    }
                }

                LazyColumn(
                    state = listState, contentPadding = PaddingValues(10.dp)
                ) {
                    item {
                        Section(title = titles[0], information = {
                            if (place.description.isEmpty()) Text(text = "Описание отсутствует.")
                            else Text(text = place.description)
                        })
                    }

                    if (place.location != null) item {
                        Section(title = titles[1], information = {
                            FaernMap(
                                place.location.latitude, place.location.longitude
                            )
                        })
                    }
                }
            }
        })
}

