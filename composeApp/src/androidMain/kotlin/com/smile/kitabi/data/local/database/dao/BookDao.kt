package com.smile.kitabi.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.smile.kitabi.data.local.database.entity.BookEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Book entities
 */
@Dao
interface BookDao {

    /**
     * Insert a new book
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    /**
     * Insert multiple books
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    /**
     * Update an existing book
     */
    @Update
    suspend fun updateBook(book: BookEntity)

    /**
     * Delete a book
     */
    @Delete
    suspend fun deleteBook(book: BookEntity)

    /**
     * Delete a book by ID
     */
    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBookById(bookId: String)

    /**
     * Get a book by ID
     */
    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): BookEntity?

    /**
     * Get a book by ISBN
     */
    @Query("SELECT * FROM books WHERE isbn = :isbn LIMIT 1")
    suspend fun getBookByIsbn(isbn: String): BookEntity?

    /**
     * Get all books as Flow for reactive updates
     */
    @Query("SELECT * FROM books ORDER BY createdAt DESC")
    fun getAllBooksFlow(): Flow<List<BookEntity>>

    /**
     * Get all books
     */
    @Query("SELECT * FROM books ORDER BY createdAt DESC")
    suspend fun getAllBooks(): List<BookEntity>

    /**
     * Search books by title
     */
    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' ORDER BY title ASC")
    suspend fun searchBooksByTitle(query: String): List<BookEntity>

    /**
     * Get books with rental end date in the future
     */
    @Query("SELECT * FROM books WHERE rentalEndDate >= datetime('now') ORDER BY rentalEndDate ASC")
    suspend fun getRentedBooks(): List<BookEntity>

    /**
     * Get total count of books
     */
    @Query("SELECT COUNT(*) FROM books")
    suspend fun getBookCount(): Int

    /**
     * Clear all books
     */
    @Query("DELETE FROM books")
    suspend fun clearAllBooks()
}

