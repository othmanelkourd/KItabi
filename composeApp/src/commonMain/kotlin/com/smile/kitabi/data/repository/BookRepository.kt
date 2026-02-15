package com.smile.kitabi.data.repository

import com.smile.kitabi.domain.model.Book
import com.smile.kitabi.domain.repository.BookRepository
import kotlinx.datetime.LocalDate

class FakeBooksRepository : BookRepository {
    override suspend fun getRentedBooks(): List<Book> {
        return listOf(
            Book(
                id = "1",
                title = "Clean Architecture",
                description = "A guide to software structure and design",
                imageUrl = "https://m.media-amazon.com/images/I/71nj3JM-igL.jpg",
                rentalEndDate = LocalDate(2026, 1, 25)
            ),
            Book(
                id = "2",
                title = "Kotlin in Action",
                description = "Deep dive into Kotlin language",
                imageUrl = "https://m.media-amazon.com/images/I/71stxGw9JgL._AC_UF1000,1000_QL80_.jpg",
                rentalEndDate = LocalDate(2026, 1, 20)
            )
        )
    }
}