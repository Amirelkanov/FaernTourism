package com.example.faerntourism.ui.screens.detailed

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.faerntourism.data.model.Article
import com.example.faerntourism.ui.components.DetailedScreenWrapper
import com.example.faerntourism.ui.screens.general.ArticleViewState
import com.example.faerntourism.ui.screens.general.ArticlesViewModel
import com.example.faerntourism.ui.screens.side.ErrorScreen
import com.example.faerntourism.ui.screens.side.LoadingScreen

@Composable
fun ArticleScreen(
    articleId: String,
    navigateBack: () -> Unit,
    articlesViewModel: ArticlesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        articlesViewModel.getArticle(articleId)
    }

    val articleViewState by articlesViewModel.articleViewStateFlow.collectAsState()


    when (val state = articleViewState) {
        is ArticleViewState.Success -> SingleArticleInfo(
            state.article,
            navigateBack,
            modifier
        )

        is ArticleViewState.Error -> {
            ErrorScreen(
                retryAction = { articlesViewModel.getArticle(articleId) },
                modifier = modifier.fillMaxSize()
            )
        }

        else -> LoadingScreen(modifier = modifier.fillMaxSize())
    }
}

@Composable
fun SingleArticleInfo(
    article: Article,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    DetailedScreenWrapper(
        mainCardTitle = article.name,
        secondaryCardTitle = "500 м",
        navigateBack = navigateBack,
        photoURL = article.imgLink,
        content = {
            LazyColumn(contentPadding = PaddingValues(10.dp)) {
                item {
                    Text(
                        text = article.description
                    )
                }
            }
        },
    )
}