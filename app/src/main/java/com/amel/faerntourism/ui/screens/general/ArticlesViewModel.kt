package com.amel.faerntourism.ui.screens.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amel.faerntourism.data.FireStoreRepository
import com.amel.faerntourism.data.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface ArticleListViewState {
    data class Success(val articles: List<Article>) : ArticleListViewState
    data class Error(val errorMsg: String) : ArticleListViewState
    data object Loading : ArticleListViewState
}

sealed interface ArticleViewState {
    data class Success(val article: Article) : ArticleViewState
    data class Error(val errorMsg: String) : ArticleViewState
    data object Loading : ArticleViewState
}

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val fireStoreRepository: FireStoreRepository
) : ViewModel() {
    private val _internalArticleListViewStateFlow =
        MutableStateFlow<ArticleListViewState>(ArticleListViewState.Loading)
    val articleListViewStateFlow = _internalArticleListViewStateFlow.asStateFlow()

    private val _internalArticleViewStateFlow =
        MutableStateFlow<ArticleViewState>(ArticleViewState.Loading)
    val articleViewStateFlow = _internalArticleViewStateFlow.asStateFlow()

    fun getArticlesData() {
        viewModelScope.launch {
            _internalArticleListViewStateFlow.update { return@update ArticleListViewState.Loading }
            fireStoreRepository.getArticles().onSuccess { articles ->
                _internalArticleListViewStateFlow.update {
                    return@update ArticleListViewState.Success(articles)
                }
            }.onFailure { e ->
                _internalArticleListViewStateFlow.update {
                    return@update ArticleListViewState.Error(e.message ?: "Unknown error occurred.")
                }
            }
        }
    }

    fun getArticle(id: String) = viewModelScope.launch {
        _internalArticleViewStateFlow.update { return@update ArticleViewState.Loading }
        fireStoreRepository.getArticle(id).onSuccess { article ->
            _internalArticleViewStateFlow.update {
                return@update ArticleViewState.Success(article)
            }
        }.onFailure { e ->
            _internalArticleViewStateFlow.update {
                return@update ArticleViewState.Error(e.message ?: "Unknown error occurred.")
            }
        }
    }
}