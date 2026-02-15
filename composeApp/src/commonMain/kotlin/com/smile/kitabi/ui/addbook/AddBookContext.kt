package com.smile.kitabi.ui.addbook

data class AddBookState(
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val rentalEndDate: String = "", // Format: YYYY-MM-DD
    val isLoading: Boolean = false,
    val titleError: String? = null,
    val dateError: String? = null,
    val isSaved: Boolean = false
)

sealed interface AddBookUiEvent {
    data class ShowSnackbar(val message: String) : AddBookUiEvent
    object NavigateBack : AddBookUiEvent
}