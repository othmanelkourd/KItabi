package com.smile.kitabi.ui.scanisbn

import com.smile.kitabi.domain.model.GoogleBookInfo

/**
 * UI State for the barcode scanning screen
 */
data class ScanIsbnUiState(
    val scannedIsbn: String = "",
    val isScanning: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isLoadingBookInfo: Boolean = false,
    val bookInfo: GoogleBookInfo? = null
)

/**
 * Events/Actions for the barcode scanning screen
 */
sealed class ScanIsbnEvent {
    data class OnBarcodeDetected(val isbn: String) : ScanIsbnEvent()
    data class OnScanningError(val error: String) : ScanIsbnEvent()
    object ClearError : ScanIsbnEvent()
    object ClearSuccess : ScanIsbnEvent()
}


