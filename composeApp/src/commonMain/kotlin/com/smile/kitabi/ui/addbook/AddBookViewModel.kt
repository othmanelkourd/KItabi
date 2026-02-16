package com.smile.kitabi.ui.addbook

import androidx.compose.ui.graphics.ImageBitmap
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

    fun onImagePicked(imageBitmap: ImageBitmap, imageBytes: ByteArray) {
        _state.update { it.copy(imageBitmap = imageBitmap, imageBytes = imageBytes) }
    }

    fun onDateSelected(date: LocalDate) {
        _state.update { it.copy(rentalEndDate = date, dateError = null, showDatePicker = false) }
    }

    fun onShowDatePicker() {
        _state.update { it.copy(showDatePicker = true) }
    }

    fun onDismissDatePicker() {
        _state.update { it.copy(showDatePicker = false) }
    }

    fun onSaveClick() {
        val currentState = _state.value

        // Validation
        var hasError = false
        if (currentState.title.isBlank()) {
            _state.update { it.copy(titleError = "Title is required") }
            hasError = true
        }

        if (currentState.rentalEndDate == null) {
            _state.update { it.copy(dateError = "Rental end date is required") }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            saveBookUseCase(
                title = currentState.title,
                description = currentState.description,
                imageBytes = currentState.imageBytes,
                rentalEndDate = currentState.rentalEndDate!!
            ).onSuccess {
                _state.update { it.copy(isLoading = false, isSaved = true) }
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