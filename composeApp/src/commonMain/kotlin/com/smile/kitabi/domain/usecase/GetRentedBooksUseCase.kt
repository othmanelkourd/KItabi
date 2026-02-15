package com.smile.kitabi.domain.usecase

import com.smile.kitabi.domain.model.Book
import com.smile.kitabi.domain.repository.BookRepository

class GetRentedBooksUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(): List<Book> =
        repository.getRentedBooks()
}