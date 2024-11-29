package com.example.faerntourism.screens

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.faerntourism.cultureArticles
import com.example.faerntourism.models.UserData
import com.example.faerntourism.places
import com.example.faerntourism.ui.components.GeneralScreenWrapper
import com.example.faerntourism.ui.components.ListItemAdditionalInfo
import com.example.faerntourism.ui.components.MyListItem
import com.example.faerntourism.ui.components.SearchBar
import com.example.faerntourism.ui.theme.FaernTourismTheme
import com.example.faerntourism.ui.theme.secondaryLight

@Composable
fun CultureScreen(
    modifier: Modifier = Modifier,
    userData: UserData? = null,
    onSignInClick: () -> Unit = {},
    onSignOut: () -> Unit = {},
    openScreen: (String) -> Unit = {},
) {
    GeneralScreenWrapper("О культуре", content = {
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

            val cultureArticles = cultureArticles()
            LazyColumn() {
                items(cultureArticles.filter {
                    it.name.contains(searchedText, ignoreCase = true)
                }) { article ->
                    MyListItem(
                        article.name, article.description, 4, article.img,
                    )
                }
            }
        }
    }, modifier = Modifier.padding(horizontal = 10.dp))
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun CultureScreenPreview() {
    FaernTourismTheme {
        CultureScreen()
    }
}
