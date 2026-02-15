package com.smile.kitabi.ui.home

import com.smile.kitabi.domain.model.Book

data class HomeState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val errorMessage: String? = null, // optional: for simple UI binding
    val isRefreshing: Boolean = false // optional: if you add pull-to-refresh later
)

sealed interface HomeUiEvent {
    data class ShowSnackbar(val message: String) : HomeUiEvent
}