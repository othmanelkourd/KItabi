package com.smile.kitabi.di

import com.smile.kitabi.data.repository.FakeBooksRepository
import com.smile.kitabi.domain.repository.BookRepository
import com.smile.kitabi.domain.usecase.GetRentedBooksUseCase
import com.smile.kitabi.domain.usecase.SaveBookUseCase
import com.smile.kitabi.domain.usecase.GetGoogleBookInfoUseCase
import com.smile.kitabi.ui.addbook.AddBookViewModel
import com.smile.kitabi.ui.home.HomeViewModel
import com.smile.kitabi.ui.scanisbn.ScanIsbnViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    // HTTP Client with JSON serialization
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    // Repositories - Default to Fake, will be overridden on Android
    single<BookRepository> { FakeBooksRepository() }

    // Use Cases
    single { GetRentedBooksUseCase(get()) }
    single { SaveBookUseCase(get()) }
    single { GetGoogleBookInfoUseCase(get()) }

    // ViewModels
    factory { HomeViewModel(get()) }
    factory { AddBookViewModel(get()) }
    factory { ScanIsbnViewModel(get()) }
}

// Platform module - can be extended in platform-specific code
val platformModule = module { }

fun initKoin(appDeclaration: KoinAppDeclaration = {}): KoinApplication =
    startKoin {
        appDeclaration()
        modules(appModule, platformModule)
    }
