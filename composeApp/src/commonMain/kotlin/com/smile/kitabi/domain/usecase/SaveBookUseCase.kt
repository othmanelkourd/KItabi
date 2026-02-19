package com.smile.kitabi.domain.usecase

import com.smile.kitabi.domain.model.Book
import com.smile.kitabi.domain.repository.BookRepository
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SaveBookUseCase(
    private val repository: BookRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        title: String,
        description: String,
        imageBytes: ByteArray,
        imageUrl: String = "",
        rentalEndDate: LocalDate,
        bookId: String? = null // Use bookId for updates, null for new books
    ): Result<Unit> = runCatching {
        val book = Book(
            id = bookId ?: Uuid.random().toString(), // Generate new ID if not provided
            title = title,
            description = description,
            imageBytes = imageBytes,
            imageUrl = imageUrl,
            rentalEndDate = rentalEndDate
        )

        // If bookId is provided, update the book; otherwise save as new
        if (bookId != null) {
            repository.updateBook(book)
        } else {
            repository.saveBook(book)
        }
    }
}