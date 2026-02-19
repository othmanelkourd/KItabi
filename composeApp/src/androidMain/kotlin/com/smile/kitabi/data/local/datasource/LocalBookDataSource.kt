package com.smile.kitabi.data.local.datasource

import com.smile.kitabi.data.local.database.KitabiDatabase
import com.smile.kitabi.data.local.database.entity.BookEntity
import com.smile.kitabi.data.local.database.mappers.toDomainModel
import com.smile.kitabi.data.local.database.mappers.toDomainModels
import com.smile.kitabi.data.local.database.mappers.toEntity
import com.smile.kitabi.domain.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local data source for books using Room database
 */
class LocalBookDataSource(
    private val database: KitabiDatabase
) {
    private val bookDao = database.bookDao()

    /**
     * Get all books as a Flow for reactive updates
     */
    fun getAllBooksFlow(): Flow<List<Book>> {
        return bookDao.getAllBooksFlow().map { entities ->
            entities.toDomainModels()
        }
    }

    /**
     * Get all books
     */
    suspend fun getAllBooks(): List<Book> {
        return bookDao.getAllBooks().toDomainModels()
    }

    /**
     * Get rented books (books with future rental end dates)
     */
    suspend fun getRentedBooks(): List<Book> {
        return bookDao.getRentedBooks().toDomainModels()
    }


    /**
     * Get a book by ISBN
     */
    suspend fun getBookByIsbn(isbn: String): Book? {
        return bookDao.getBookByIsbn(isbn)?.toDomainModel()
    }

    /**
     * Save a book (insert or update)
     */
    suspend fun saveBook(book: Book) {
        val entity = book.toEntity()
        bookDao.insertBook(entity)
    }

    /**
     * Save multiple books
     */
    suspend fun saveBooks(books: List<Book>) {
        val entities = books.map { it.toEntity() }
        bookDao.insertBooks(entities)
    }

    /**
     * Update a book
     */
    suspend fun updateBook(book: Book) {
        val entity = book.toEntity()
        bookDao.updateBook(entity)
    }

    /**
     * Delete a book
     */
    suspend fun deleteBook(book: Book) {
        val entity = book.toEntity()
        bookDao.deleteBook(entity)
    }

    /**
     * Delete a book by ISBN
     */
    suspend fun deleteBookByIsbn(isbn: String) {
        bookDao.deleteBookByIsbn(isbn)
    }

    /**
     * Search books by title
     */
    suspend fun searchBooksByTitle(query: String): List<Book> {
        return bookDao.searchBooksByTitle(query).toDomainModels()
    }

    /**
     * Get total count of books
     */
    suspend fun getBookCount(): Int {
        return bookDao.getBookCount()
    }

    /**
     * Clear all books
     */
    suspend fun clearAllBooks() {
        bookDao.clearAllBooks()
    }
}

