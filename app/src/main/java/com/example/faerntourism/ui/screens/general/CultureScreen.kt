package com.example.faerntourism.ui.screens.general

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.faerntourism.Culture
import com.example.faerntourism.FaernDestination
import com.example.faerntourism.data.cultureArticles
import com.example.faerntourism.data.model.UserData
import com.example.faerntourism.ui.components.GeneralScreenWrapper
import com.example.faerntourism.ui.components.MyListItem
import com.example.faerntourism.ui.components.SearchBar

@Composable
fun CultureScreen(
    onBottomTabSelected: (FaernDestination) -> Unit,
    onArticleClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    userData: UserData? = null,
    onSignInClick: () -> Unit = {},
    onSignOut: () -> Unit = {},
) {
    GeneralScreenWrapper(
        currentScreen = Culture,
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

                val cultureArticles = cultureArticles()
                LazyColumn() {
                    itemsIndexed(cultureArticles.filter {
                        it.name.contains(searchedText, ignoreCase = true)
                    }) { index, article ->
                        MyListItem(
                            article.name, article.description, 4, article.img,
                            modifier = Modifier.clickable(
                                // TODO: вместо индекса надо будет айдишник из бд пихать
                                onClick = { onArticleClick(index.toString()) }
                            )
                        )
                    }
                }
            }
        }, modifier = Modifier.padding(horizontal = 10.dp)
    )
}
