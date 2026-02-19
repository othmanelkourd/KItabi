package com.smile.kitabi.data.repository

import com.smile.kitabi.data.local.datasource.LocalBookDataSource
import com.smile.kitabi.domain.model.Book
import com.smile.kitabi.domain.repository.BookRepository

/**
 * Room Database implementation of BookRepository
 * Uses local SQLite database for persistence
 */
class RoomBooksRepository(
    private val localDataSource: LocalBookDataSource
) : BookRepository {

    override suspend fun getRentedBooks(): List<Book> {
        return localDataSource.getRentedBooks()
    }

    override suspend fun saveBook(book: Book) {
        localDataSource.saveBook(book)
    }

    /**
     * Additional methods for Room-specific functionality
     */

    suspend fun getAllBooks(): List<Book> {
        return localDataSource.getAllBooks()
    }


    suspend fun getBookByIsbn(isbn: String): Book? {
        return localDataSource.getBookByIsbn(isbn)
    }

    suspend fun updateBook(book: Book) {
        localDataSource.updateBook(book)
    }

    suspend fun deleteBook(book: Book) {
        localDataSource.deleteBook(book)
    }

    suspend fun searchBooksByTitle(query: String): List<Book> {
        return localDataSource.searchBooksByTitle(query)
    }

    suspend fun getBookCount(): Int {
        return localDataSource.getBookCount()
    }

    suspend fun clearAllBooks() {
        localDataSource.clearAllBooks()
    }
}

