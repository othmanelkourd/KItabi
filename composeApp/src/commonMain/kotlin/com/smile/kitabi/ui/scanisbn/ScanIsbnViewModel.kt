package com.smile.kitabi.ui.scanisbn

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScanIsbnViewModel : ViewModel() {
    private val _state = MutableStateFlow(ScanIsbnUiState())
    val state: StateFlow<ScanIsbnUiState> = _state.asStateFlow()

    fun onBarcodeDetected(isbn: String) {
        _state.value = _state.value.copy(
            scannedIsbn = isbn,
            successMessage = "ISBN detected: $isbn"
        )
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

