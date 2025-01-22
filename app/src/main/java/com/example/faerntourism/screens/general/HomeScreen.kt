package com.example.faerntourism.screens.general


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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.faerntourism.FaernDestination
import com.example.faerntourism.Home
import com.example.faerntourism.data.model.UserData
import com.example.faerntourism.data.places
import com.example.faerntourism.ui.components.GeneralScreenWrapper
import com.example.faerntourism.ui.components.ListItemAdditionalInfo
import com.example.faerntourism.ui.components.MyListItem
import com.example.faerntourism.ui.components.SearchBar
import com.example.faerntourism.ui.theme.secondaryLight

@Composable
fun HomeScreen(
    onBottomTabSelected: (FaernDestination) -> Unit,
    onPlaceClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    userData: UserData? = null,
    onSignInClick: () -> Unit = {},
    onSignOut: () -> Unit = {},
) {

    GeneralScreenWrapper(
        currentScreen = Home,
        onBottomTabSelected = onBottomTabSelected,
        content = {
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

                val places = places()
                LazyColumn {
                    itemsIndexed(places.filter {
                        it.name.contains(searchedText, ignoreCase = true)
                    }) { index, place ->
                        MyListItem(
                            place.name, place.description, 2, place.img,
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
        },
        modifier = Modifier.padding(horizontal = 10.dp)
    )
}

