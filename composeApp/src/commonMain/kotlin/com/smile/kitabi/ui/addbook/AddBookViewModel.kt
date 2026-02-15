package com.smile.kitabi.ui.addbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smile.kitabi.domain.usecase.SaveBookUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent

class AddBookViewModel(
    private val saveBookUseCase: SaveBookUseCase
) : ViewModel(), KoinComponent {

    private val _state = MutableStateFlow(AddBookState())
    val state: StateFlow<AddBookState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<AddBookUiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<AddBookUiEvent> = _events.asSharedFlow()

    fun onTitleChange(title: String) {
        _state.update { it.copy(title = title, titleError = null) }
    }

    fun onDescriptionChange(description: String) {
        _state.update { it.copy(description = description) }
    }

    fun onImageUrlChange(imageUrl: String) {
        _state.update { it.copy(imageUrl = imageUrl) }
    }

    fun onRentalEndDateChange(date: String) {
        _state.update { it.copy(rentalEndDate = date, dateError = null) }
    }

    fun onSaveClick() {
        val currentState = _state.value

        // Validation
        var hasError = false
        if (currentState.title.isBlank()) {
            _state.update { it.copy(titleError = "Title is required") }
            hasError = true
        }

        val parsedDate = try {
            LocalDate.parse(currentState.rentalEndDate)
        } catch (e: Exception) {
            _state.update { it.copy(dateError = "Invalid date format (YYYY-MM-DD)") }
            hasError = true
            null
        }

        if (hasError) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            saveBookUseCase(
                title = currentState.title,
                description = currentState.description,
                imageUrl = currentState.imageUrl,
                rentalEndDate = parsedDate!!
            ).onSuccess {
                _state.update { it.copy(isLoading = false, isSaved = true) }
                _events.tryEmit(AddBookUiEvent.ShowSnackbar("Book saved successfully!"))
                _events.tryEmit(AddBookUiEvent.NavigateBack)
            }.onFailure { e ->
                val msg = e.message ?: "Failed to save book"
                _state.update { it.copy(isLoading = false) }
                _events.tryEmit(AddBookUiEvent.ShowSnackbar(msg))
            }
        }
    }

    fun onCancelClick() {
        _events.tryEmit(AddBookUiEvent.NavigateBack)
    }
}