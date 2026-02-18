package com.smile.kitabi.domain.usecase

import com.smile.kitabi.domain.model.GoogleBookInfo
import com.smile.kitabi.domain.model.GoogleBooksResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class GetGoogleBookInfoUseCase(
    private val httpClient: HttpClient
) {
    suspend operator fun invoke(isbn: String): Result<GoogleBookInfo> = runCatching {
        // Clean ISBN - remove any dashes or spaces
        val cleanIsbn = isbn.replace("-", "").replace(" ", "").trim()

        if (cleanIsbn.isEmpty()) {
            throw IllegalArgumentException("ISBN cannot be empty")
        }

        // Query Google Books API
        val response: GoogleBooksResponse = httpClient.get(GOOGLE_BOOKS_API_URL) {
            parameter("q", "isbn:$cleanIsbn")
            parameter("key", GOOGLE_BOOKS_API_KEY)
            parameter("maxResults", 1)
        }.body()

        // Find the first book result
        val bookVolume = response.items?.firstOrNull()
            ?: throw Exception("No book found for ISBN: $isbn")

        val volumeInfo = bookVolume.volumeInfo

        // Extract book information
        GoogleBookInfo(
            title = volumeInfo.title,
            authors = volumeInfo.authors ?: emptyList(),
            description = volumeInfo.description ?: "",
            thumbnailUrl = volumeInfo.imageLinks?.thumbnail ?: "",
            publisher = volumeInfo.publisher ?: "",
            publishedDate = volumeInfo.publishedDate ?: "",
            pageCount = volumeInfo.pageCount ?: 0,
            isbn = isbn
        )
    }

    companion object {
        private const val GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes"
        // TODO: Replace with your actual Google Books API key
        private const val GOOGLE_BOOKS_API_KEY = "AIzaSyCm8vO_nE9IebMEcu0rbBbZRasj-orZ3fQ"
    }
}

