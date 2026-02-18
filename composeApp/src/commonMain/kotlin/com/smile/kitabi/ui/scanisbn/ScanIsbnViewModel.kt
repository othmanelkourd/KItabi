package com.smile.kitabi.ui.scanisbn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smile.kitabi.domain.usecase.GetGoogleBookInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanIsbnViewModel(
    private val getGoogleBookInfo: GetGoogleBookInfoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ScanIsbnUiState())
    val state: StateFlow<ScanIsbnUiState> = _state.asStateFlow()

    fun onBarcodeDetected(isbn: String) {
        _state.value = _state.value.copy(
            scannedIsbn = isbn,
            successMessage = "ISBN detected: $isbn"
        )

        // Fetch book info from Google Books API
        fetchBookInfo(isbn)
    }

    private fun fetchBookInfo(isbn: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingBookInfo = true, errorMessage = null) }

            getGoogleBookInfo(isbn)
                .onSuccess { bookInfo ->
                    _state.update {
                        it.copy(
                            isLoadingBookInfo = false,
                            bookInfo = bookInfo,
                            successMessage = "Book info loaded: ${bookInfo.title}"
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoadingBookInfo = false,
                            errorMessage = "Failed to fetch book info: ${error.message}"
                        )
                    }
                }
        }
    }

    fun onScanningError(error: String) {
        _state.value = _state.value.copy(
            errorMessage = error,
            isScanning = false
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }

    fun clearSuccess() {
        _state.value = _state.value.copy(successMessage = null)
    }
}

