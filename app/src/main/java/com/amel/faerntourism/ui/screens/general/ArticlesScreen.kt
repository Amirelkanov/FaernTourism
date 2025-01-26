package com.amel.faerntourism.ui.screens.general

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.amel.faerntourism.Articles
import com.amel.faerntourism.FaernDestination
import com.amel.faerntourism.data.model.Article
import com.amel.faerntourism.ui.components.GeneralScreenWrapper
import com.amel.faerntourism.ui.components.FaernListItem
import com.amel.faerntourism.ui.components.SearchBar
import com.amel.faerntourism.ui.screens.side.ErrorScreen
import com.amel.faerntourism.ui.screens.side.LoadingScreen

@Composable
fun ArticlesScreen(
    onBottomTabSelected: (FaernDestination) -> Unit,
    onArticleClick: (String) -> Unit,
    articlesViewModel: ArticlesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        articlesViewModel.getArticlesData()
    }

    val placesViewState by articlesViewModel.articleListViewStateFlow.collectAsState()


    GeneralScreenWrapper(
        currentScreen = Articles, onBottomTabSelected = onBottomTabSelected, content = {
            when (val state = placesViewState) {
                is ArticleListViewState.Success -> ArticlesFeedScreen(
                    state.articles, onArticleClick, modifier
                )

                is ArticleListViewState.Error -> ErrorScreen(
                    articlesViewModel::getArticlesData, modifier = modifier.fillMaxSize()
                )

                else -> LoadingScreen(modifier = modifier.fillMaxSize())
            }
        }, modifier = Modifier.padding(horizontal = 10.dp)
    )
}


@Composable
fun ArticlesFeedScreen(
    articles: List<Article>, onArticleClick: (String) -> Unit, modifier: Modifier = Modifier
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
            items(articles.filter {
                it.name.contains(searchedText, ignoreCase = true)
            }) { article ->
                FaernListItem(
                    article.name,
                    article.description,
                    4,
                    article.imgLink,
                    modifier = Modifier.clickable(
                        onClick = { onArticleClick(article.id) })
                )
            }
        }
    }
}