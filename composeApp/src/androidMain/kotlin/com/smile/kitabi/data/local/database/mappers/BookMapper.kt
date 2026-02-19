package com.smile.kitabi.data.local.database.mappers

import com.smile.kitabi.data.local.database.entity.BookEntity
import com.smile.kitabi.domain.model.Book

/**
 * Mappers between BookEntity (Room) and Book (Domain model)
 */

fun BookEntity.toDomainModel(): Book {
    return Book(
        id = isbn,
        title = title,
        description = description,
        imageBytes = imageBytes,
        rentalEndDate = rentalEndDate
    )
}

fun Book.toEntity(): BookEntity {
    return BookEntity(
        isbn = id,
        title = title,
        description = description,
        imageBytes = imageBytes,
        rentalEndDate = rentalEndDate
    )
}

fun List<BookEntity>.toDomainModels(): List<Book> {
    return map { it.toDomainModel() }
}

fun List<Book>.toEntities(): List<BookEntity> {
    return map { it.toEntity() }
}

