package com.example.faerntourism.screens.detailed

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.faerntourism.cultureArticles
import com.example.faerntourism.models.CultureArticle
import com.example.faerntourism.ui.components.DetailedScreenWrapper
import com.example.faerntourism.ui.theme.FaernTourismTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    article: CultureArticle,
    modifier: Modifier = Modifier,
) {
    DetailedScreenWrapper(
        article.name,
        "500 м",
        article.img,
        content = {
            LazyColumn(contentPadding = PaddingValues(10.dp)) {
                item {
                    Text(
                        text = article.description
                    )
                }
            }
        }
    )
}


@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun ArticleScreenPreview() {
    FaernTourismTheme {
        ArticleScreen(cultureArticles()[0])
    }
}
