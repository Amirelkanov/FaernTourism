package com.example.faerntourism.screens.general

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.faerntourism.TOURS_SCREEN
import com.example.faerntourism.models.UserData
import com.example.faerntourism.tours
import com.example.faerntourism.ui.components.GeneralScreenWrapper
import com.example.faerntourism.ui.components.ListItemAdditionalInfo
import com.example.faerntourism.ui.components.MyListItem
import com.example.faerntourism.ui.components.SearchBar
import com.example.faerntourism.ui.theme.AppTypography
import com.example.faerntourism.ui.theme.FaernTourismTheme
import com.example.faerntourism.ui.theme.secondaryLight

@Composable
fun ToursScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    userData: UserData? = null,
    onSignInClick: () -> Unit = {},
    onSignOut: () -> Unit = {},
) {
    GeneralScreenWrapper("Туры", content = {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
        ) {

            val textState = remember {
                mutableStateOf(TextFieldValue(""))
            }

            SearchBar(state = textState, trailingContent = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                )
            })

            val uriHandler = LocalUriHandler.current
            val searchedText = textState.value.text

            val tours = tours()
            LazyColumn {
                items(tours.filter {
                    it.name.contains(searchedText, ignoreCase = true)
                }) { tour ->
                    MyListItem(
                        tour.name, tour.description, 2, tour.img,
                        additionalInfo = {
                            ListItemAdditionalInfo(
                                icon = {
                                    Icon(
                                        Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = secondaryLight
                                    )
                                },
                                text = "от " + tour.startDate
                            )
                        },
                        trailingContent = {
                            Text(
                                "${tour.price}₽",
                                style = AppTypography.titleMedium,
                                maxLines = 1,
                                color = secondaryLight
                            )
                        },
                        modifier.clickable(onClick = { uriHandler.openUri("https://www.google.com") })
                    )
                }
            }
        }
    }, TOURS_SCREEN, openScreen, modifier = Modifier.padding(horizontal = 10.dp))
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun ToursScreenPreview() {
    FaernTourismTheme {
        ToursScreen({})
    }
}

