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
import com.example.faerntourism.data.model.CultureArticle
import com.example.faerntourism.ui.components.GeneralScreenWrapper
import com.example.faerntourism.ui.components.MyListItem
import com.example.faerntourism.ui.components.SearchBar
import com.example.faerntourism.ui.screens.side.ErrorScreen
import com.example.faerntourism.ui.screens.side.LoadingScreen

@Composable
fun CultureScreen(
    onBottomTabSelected: (FaernDestination) -> Unit,
    onArticleClick: (String) -> Unit,
    articlesViewState: ArticlesViewState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GeneralScreenWrapper(
        currentScreen = Culture, onBottomTabSelected = onBottomTabSelected, content = {
            when (articlesViewState) {
                is ArticlesViewState.Success -> CultureArticlesFeedScreen(
                    articlesViewState.articles, onArticleClick, modifier
                )

                is ArticlesViewState.Error -> ErrorScreen(
                    retryAction, modifier = modifier.fillMaxSize()
                )

                else -> LoadingScreen(modifier = modifier.fillMaxSize())
            }
        }, modifier = Modifier.padding(horizontal = 10.dp)
    )
}


@Composable
fun CultureArticlesFeedScreen(
    articles: List<CultureArticle>, onArticleClick: (String) -> Unit, modifier: Modifier = Modifier
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

        LazyColumn() {
            itemsIndexed(articles.filter {
                it.name.contains(searchedText, ignoreCase = true)
            }) { index, article ->
                MyListItem(
                    article.name,
                    article.description,
                    4,
                    null,

                    modifier = Modifier.clickable(
                        // TODO: вместо индекса надо будет айдишник из бд пихать
                        onClick = { onArticleClick(index.toString()) })
                )
            }
        }
    }
}