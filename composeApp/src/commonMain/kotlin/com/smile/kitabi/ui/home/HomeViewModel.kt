package com.smile.kitabi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smile.kitabi.domain.usecase.GetRentedBooksUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent

class HomeViewModel(
    private val getRentedBooks: GetRentedBooksUseCase
) : ViewModel(), KoinComponent {

    private val _state = MutableStateFlow(HomeState(isLoading = true))
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<HomeUiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<HomeUiEvent> = _events.asSharedFlow()


    init {
        loadBooks()
    }

    fun refreshBooks() {
        loadBooks()
    }

    private fun loadBooks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { getRentedBooks() }
                .onSuccess { books ->
                    _state.update { it.copy(isLoading = false, books = books) }
                }
                .onFailure { e ->
                    val msg = e.message ?: "Something went wrong"
                    _state.update { it.copy(isLoading = false, errorMessage = msg) }
                    _events.tryEmit(HomeUiEvent.ShowSnackbar(msg))
                }
        }
    }
}