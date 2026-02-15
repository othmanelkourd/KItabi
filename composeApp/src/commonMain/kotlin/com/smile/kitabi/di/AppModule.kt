package com.smile.kitabi.di

import com.smile.kitabi.data.repository.FakeBooksRepository
import com.smile.kitabi.domain.repository.BookRepository
import com.smile.kitabi.domain.usecase.GetRentedBooksUseCase
import com.smile.kitabi.ui.home.HomeViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    single<BookRepository> { FakeBooksRepository() }
    single { GetRentedBooksUseCase(get()) }
    factory { HomeViewModel(get()) }
}

// Optional platform module (can be empty for now)
val platformModule = module { }

fun initKoin(appDeclaration: KoinAppDeclaration = {}): KoinApplication =
    startKoin {
        appDeclaration()
        modules(appModule, platformModule)
    }
