package com.smile.kitabi.data.repository

import com.smile.kitabi.domain.model.Book
import com.smile.kitabi.domain.repository.BookRepository
import kotlinx.datetime.LocalDate

class FakeBooksRepository : BookRepository {

    private val books = mutableListOf<Book>()

    init {
        // Start with empty books list to show empty state
        // books.addAll(
        //     listOf(
        //         Book(
        //             id = "1",
        //             title = "Clean Architecture",
        //             description = "A guide to software structure and design",
        //             imageBytes = byteArrayOf(),
        //             rentalEndDate = LocalDate(2026, 1, 25)
        //         ),
        //         Book(
        //             id = "2",
        //             title = "Kotlin in Action",
        //             description = "Deep dive into Kotlin language",
        //             imageBytes = byteArrayOf(),
        //             rentalEndDate = LocalDate(2026, 1, 20)
        //         )
        //     )
        // )
    }

    override suspend fun getRentedBooks(): List<Book> {
        return books.toList()
    }

    override suspend fun saveBook(book: Book) {
        books.add(book)
    }

    override suspend fun updateBook(book: Book) {
        // Find and replace the book with the same ISBN
        val index = books.indexOfFirst { it.id == book.id }
        if (index != -1) {
            books[index] = book
        } else {
            // If not found, add it as new
            books.add(book)
        }
    }
}