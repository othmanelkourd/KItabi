package com.smile.kitabi.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

data class Book(
    val id: String,
    val title: String,
    val description: String,
    val imageBytes: ByteArray,
    val imageUrl: String = "", // URL from Google Books API
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
        if (imageUrl != other.imageUrl) return false
        if (rentalEndDate != other.rentalEndDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + imageBytes.contentHashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + rentalEndDate.hashCode()
        return result
    }
}

fun Book.remainingDays(): Int {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val daysRemaining = (rentalEndDate.toEpochDays() - today.toEpochDays()).toInt()
    return maxOf(daysRemaining, 0) // Return 0 if date has passed
}