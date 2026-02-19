package com.smile.kitabi.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

/**
 * Room Entity representing a Book in the local database
 */
@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey
    val id: String, // Random ID as primary key, allows books without ISBN
    val isbn: String = "", // Optional ISBN
    val title: String,
    val description: String = "",
    val imageBytes: ByteArray = byteArrayOf(),
    val imageUrl: String = "", // URL from Google Books API
    val rentalEndDate: LocalDate,
    val publisher: String = "",
    val authors: String = "", // Stored as comma-separated string
    val pageCount: Int = 0,
    val publishedDate: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BookEntity

        if (id != other.id) return false
        if (isbn != other.isbn) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (!imageBytes.contentEquals(other.imageBytes)) return false
        if (imageUrl != other.imageUrl) return false
        if (rentalEndDate != other.rentalEndDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + isbn.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + imageBytes.contentHashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + rentalEndDate.hashCode()
        return result
    }
}

