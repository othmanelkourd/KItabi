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
        rentalEndDate: LocalDate
    ): Result<Unit> = runCatching {
        val book = Book(
            id = Uuid.random().toString(),
            title = title,
            description = description,
            imageBytes = imageBytes,
            imageUrl = imageUrl,
            rentalEndDate = rentalEndDate
        )
        repository.saveBook(book)
    }
}