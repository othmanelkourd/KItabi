package com.smile.kitabi.di

import android.content.Context
import com.smile.kitabi.data.local.database.KitabiDatabase
import com.smile.kitabi.data.local.datasource.LocalBookDataSource
import com.smile.kitabi.data.repository.RoomBooksRepository
import com.smile.kitabi.domain.repository.BookRepository
import org.koin.dsl.module

/**
 * Android platform-specific DI module
 * Provides Room database and repository implementations
 */
fun androidModule(context: Context) = module {
    // Room Database singleton
    single {
        KitabiDatabase.getInstance(context)
    }

    // Local data source
    single {
        LocalBookDataSource(get())
    }

    // Room-based repository
    single<BookRepository> {
        RoomBooksRepository(get())
    }
}

