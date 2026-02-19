package com.smile.kitabi.data.local.database.mappers

import com.smile.kitabi.data.local.database.entity.BookEntity
import com.smile.kitabi.domain.model.Book
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Mappers between BookEntity (Room) and Book (Domain model)
 */

fun BookEntity.toDomainModel(): Book {
    return Book(
        id = id,
        title = title,
        description = description,
        imageBytes = imageBytes,
        imageUrl = imageUrl,
        rentalEndDate = rentalEndDate
    )
}

@OptIn(ExperimentalUuidApi::class)
fun Book.toEntity(): BookEntity {
    return BookEntity(
        id = if (id.isEmpty()) Uuid.random().toString() else id,
        isbn = "", // ISBN is optional for manually added books
        title = title,
        description = description,
        imageBytes = imageBytes,
        imageUrl = imageUrl,
        rentalEndDate = rentalEndDate
    )
}

fun List<BookEntity>.toDomainModels(): List<Book> {
    return map { it.toDomainModel() }
}

fun List<Book>.toEntities(): List<BookEntity> {
    return map { it.toEntity() }
}

