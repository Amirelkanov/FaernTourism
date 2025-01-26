package com.example.faerntourism.ui.screens.detailed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.faerntourism.data.model.Article
import com.example.faerntourism.ui.components.FaernCard
import com.example.faerntourism.ui.screens.general.ArticleViewState
import com.example.faerntourism.ui.screens.general.ArticlesViewModel
import com.example.faerntourism.ui.screens.side.ErrorScreen
import com.example.faerntourism.ui.screens.side.LoadingScreen
import kotlinx.coroutines.launch

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
    val (toolbarMinHeight, toolbarMaxHeight) = 56.dp to 200.dp

    val toolbarMinPx = with(LocalDensity.current) { toolbarMinHeight.toPx() }
    val toolbarMaxPx = with(LocalDensity.current) { toolbarMaxHeight.toPx() }
    val toolbarRange = toolbarMaxPx - toolbarMinPx

    val collapseOffsetPx = remember { mutableFloatStateOf(0f) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val animatedHeight by animateDpAsState(
        targetValue = with(LocalDensity.current) {
            (toolbarMaxPx - collapseOffsetPx.floatValue).toDp()
        }, label = "animatedHeight"
    )

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val oldOffset = collapseOffsetPx.floatValue
                val newOffset = (oldOffset + -delta).coerceIn(0f, toolbarRange)
                collapseOffsetPx.floatValue = newOffset
                // The portion of scroll consumed by the top bar collapse
                return Offset(x = 0f, y = oldOffset - newOffset)
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(top = animatedHeight),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = article.description,
                    style = TextStyle(
                        color = colorScheme.onSurface,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(20.dp)
                )
            }
        }

        FaernCard(
            mainTitle = article.name,
            secondaryTitle = article.creationDate,
            photoURL = article.imgLink,
            navigateBack = navigateBack,
            modifier = Modifier
                .height(animatedHeight)
                .align(Alignment.TopCenter)
                .clickable {
                    coroutineScope.launch {
                        scrollUp(collapseOffsetPx, listState)
                    }
                }
        )

        val showButton by remember {
            derivedStateOf {
                listState.firstVisibleItemScrollOffset > 0
            }
        }

        AnimatedVisibility(
            visible = showButton,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing)
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(16.dp),
        ) {
            FloatingActionButton(
                containerColor = colorScheme.primary,

                onClick = {
                    coroutineScope.launch {
                        scrollUp(collapseOffsetPx, listState)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    tint = colorScheme.onPrimary,
                    contentDescription = "up"
                )
            }

        }
    }
}

private suspend fun scrollUp(collapseOffsetPx: MutableFloatState, listState: LazyListState) {
    collapseOffsetPx.floatValue = 0f
    listState.animateScrollToItem(0)
}