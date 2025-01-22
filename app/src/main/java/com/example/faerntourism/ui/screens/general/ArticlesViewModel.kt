package com.example.faerntourism.ui.screens.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faerntourism.data.FireStoreRepository
import com.example.faerntourism.data.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface ArticlesViewState {
    data class Success(val articles: List<Article>) : ArticlesViewState
    data class Error(val errorMsg: String) : ArticlesViewState
    data object Loading : ArticlesViewState
}

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val fireStoreRepository: FireStoreRepository
) : ViewModel() {
    private val _internalArticlesViewStateFlow =
        MutableStateFlow<ArticlesViewState>(ArticlesViewState.Loading)
    val articlesViewStateFlow = _internalArticlesViewStateFlow.asStateFlow()

    init {
        getArticlesData()
    }

    fun getArticlesData() {
        viewModelScope.launch {
            _internalArticlesViewStateFlow.update { return@update ArticlesViewState.Loading }
            fireStoreRepository.getArticles().onSuccess { articles ->
                _internalArticlesViewStateFlow.update {
                    return@update ArticlesViewState.Success(articles)
                }
            }.onFailure { e ->
                _internalArticlesViewStateFlow.update {
                    return@update ArticlesViewState.Error(e.message ?: "Unknown error occurred.")
                }
            }
        }
    }
}