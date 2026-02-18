package com.smile.kitabi.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response from Google Books API
 */
@Serializable
data class GoogleBooksResponse(
    val items: List<BookVolume>? = null,
    val totalItems: Int = 0
)

/**
 * Book volume data from Google Books API
 */
@Serializable
data class BookVolume(
    val id: String,
    val volumeInfo: VolumeInfo
)

/**
 * Volume information containing book details
 */
@Serializable
data class VolumeInfo(
    val title: String,
    val authors: List<String>? = null,
    val description: String? = null,
    val imageLinks: ImageLinks? = null,
    val publisher: String? = null,
    @SerialName("publishedDate")
    val publishedDate: String? = null,
    @SerialName("pageCount")
    val pageCount: Int? = null,
    val industryIdentifiers: List<IndustryIdentifier>? = null
)

/**
 * Image links for book cover
 */
@Serializable
data class ImageLinks(
    val thumbnail: String? = null,
    val small: String? = null,
    val medium: String? = null,
    val large: String? = null
)

/**
 * Industry identifier (ISBN, etc.)
 */
@Serializable
data class IndustryIdentifier(
    val type: String,
    val identifier: String
)

/**
 * Mapped book data from Google Books API
 */
data class GoogleBookInfo(
    val title: String,
    val authors: List<String> = emptyList(),
    val description: String = "",
    val thumbnailUrl: String = "",
    val publisher: String = "",
    val publishedDate: String = "",
    val pageCount: Int = 0,
    val isbn: String = ""
)

