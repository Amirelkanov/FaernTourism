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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.faerntourism.FaernDestination
import com.example.faerntourism.Tours
import com.example.faerntourism.data.model.Tour
import com.example.faerntourism.ui.components.FaernListItem
import com.example.faerntourism.ui.components.GeneralScreenWrapper
import com.example.faerntourism.ui.components.ListItemAdditionalInfo
import com.example.faerntourism.ui.components.SearchBar
import com.example.faerntourism.ui.screens.side.ErrorScreen
import com.example.faerntourism.ui.screens.side.LoadingScreen
import com.example.faerntourism.ui.theme.AppTypography
import com.example.faerntourism.ui.theme.FaernTourismTheme

@Composable
fun ToursScreen(
    onBottomTabSelected: (FaernDestination) -> Unit,
    toursViewModel: ToursViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        toursViewModel.getTours()
    }

    val toursViewState by toursViewModel.tourListStateFlow.collectAsState()

    GeneralScreenWrapper(
        currentScreen = Tours,
        onBottomTabSelected = onBottomTabSelected,
        content = {
            when (val state = toursViewState) {
                is TourListViewState.Success -> ToursFeedScreen(
                    state.tours,
                    modifier
                )

                is TourListViewState.Error -> ErrorScreen(
                    toursViewModel::getTours,
                    modifier = modifier.fillMaxSize()
                )

                else -> LoadingScreen(modifier = modifier.fillMaxSize())
            }
        },
        modifier = Modifier.padding(horizontal = 10.dp)
    )
}

@Composable
fun ToursFeedScreen(
    tours: List<Tour>,
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

        val uriHandler = LocalUriHandler.current
        val searchedText = textState.value.text

        LazyColumn {
            items(tours.filter {
                it.name.contains(searchedText, ignoreCase = true)
            }) { tour ->
                FaernListItem(
                    tour.name, tour.description, 2, tour.imgLink,
                    additionalInfo = {
                        ListItemAdditionalInfo(
                            icon = {
                                Icon(
                                    Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = colorScheme.secondary
                                )
                            },
                            text = "от " + tour.date
                        )
                    },
                    trailingContent = {
                        Text(
                            text = tour.price,
                            style = AppTypography.titleSmall,
                            maxLines = 1,
                            color = colorScheme.secondary,
                        )
                    },
                    modifier.clickable(onClick = { uriHandler.openUri(tour.link) })
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun ToursScreenPreview() {
    FaernTourismTheme {
        ToursScreen({})
    }
}

