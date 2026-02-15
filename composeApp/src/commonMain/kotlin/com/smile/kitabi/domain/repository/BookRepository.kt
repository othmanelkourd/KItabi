package com.smile.kitabi.domain.repository

import com.smile.kitabi.domain.model.Book

interface BookRepository {
    suspend fun getRentedBooks(): List<Book>
    suspend fun saveBook(book: Book)
}