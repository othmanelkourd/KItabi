package com.smile.kitabi.domain.model

import kotlinx.datetime.LocalDate

data class Book(
    val id: String,
    val title: String,
    val description: String,
    val imageBytes: ByteArray,
    val rentalEndDate: LocalDate
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Book

        if (id != other.id) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (!imageBytes.contentEquals(other.imageBytes)) return false
        if (rentalEndDate != other.rentalEndDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + imageBytes.contentHashCode()
        result = 31 * result + rentalEndDate.hashCode()
        return result
    }
}

fun Book.remainingDays(): Int {

    return 10
}