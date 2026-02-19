package com.smile.kitabi.ui.addbook

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.datetime.LocalDate

data class AddBookState(
    val title: String = "",
    val description: String = "",
    val imageBitmap: ImageBitmap? = null,
    val imageBytes: ByteArray = byteArrayOf(),
    val imageUrl: String = "", // URL from Google Books API
    val rentalEndDate: LocalDate? = null,
    val isLoading: Boolean = false,
    val titleError: String? = null,
    val dateError: String? = null,
    val isSaved: Boolean = false,
    val showDatePicker: Boolean = false,
    val editingBookId: String? = null // Track if editing an existing book (ID)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AddBookState

        if (title != other.title) return false
        if (description != other.description) return false
        if (imageBitmap != other.imageBitmap) return false
        if (!imageBytes.contentEquals(other.imageBytes)) return false
        if (imageUrl != other.imageUrl) return false
        if (rentalEndDate != other.rentalEndDate) return false
        if (isLoading != other.isLoading) return false
        if (titleError != other.titleError) return false
        if (dateError != other.dateError) return false
        if (isSaved != other.isSaved) return false
        if (showDatePicker != other.showDatePicker) return false
        if (editingBookId != other.editingBookId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (imageBitmap?.hashCode() ?: 0)
        result = 31 * result + imageBytes.contentHashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + (rentalEndDate?.hashCode() ?: 0)
        result = 31 * result + isLoading.hashCode()
        result = 31 * result + (titleError?.hashCode() ?: 0)
        result = 31 * result + (dateError?.hashCode() ?: 0)
        result = 31 * result + isSaved.hashCode()
        result = 31 * result + showDatePicker.hashCode()
        result = 31 * result + (editingBookId?.hashCode() ?: 0)
        return result
    }
}

sealed interface AddBookUiEvent {
    data class ShowSnackbar(val message: String) : AddBookUiEvent
    object NavigateBack : AddBookUiEvent
}