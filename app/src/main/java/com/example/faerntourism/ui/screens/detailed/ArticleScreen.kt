package com.example.faerntourism.ui.screens.detailed

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.faerntourism.data.cultureArticles
import com.example.faerntourism.ui.components.DetailedScreenWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    articleId: String? = cultureArticles().first().id.toString(),
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val article = cultureArticles()[articleId?.toInt()!!] // TODO: надо переделывать


    DetailedScreenWrapper(
        mainCardTitle = article.name,
        secondaryCardTitle = "500 м",
        navigateBack = navigateBack,
        content = {
            LazyColumn(contentPadding = PaddingValues(10.dp)) {
                item {
                    Text(
                        text = article.description
                    )
                }
            }
        },
        painterCard = null,
    )
}
