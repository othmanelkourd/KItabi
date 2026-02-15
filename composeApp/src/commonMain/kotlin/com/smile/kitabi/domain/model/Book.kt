package com.smile.kitabi.domain.model

import kotlinx.datetime.LocalDate

data class Book(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val rentalEndDate: LocalDate
)

fun Book.remainingDays(): Int {

    return 10
}