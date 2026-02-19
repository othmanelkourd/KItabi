package com.smile.kitabi.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smile.kitabi.data.local.database.dao.BookDao
import com.smile.kitabi.data.local.database.entity.BookEntity
import com.smile.kitabi.data.local.database.converters.DateConverter

/**
 * Room Database for KItabi application
 */
@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class KitabiDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        private const val DATABASE_NAME = "kitabi_database"

        @Volatile
        private var instance: KitabiDatabase? = null

        fun getInstance(context: Context): KitabiDatabase =
            instance ?: synchronized(this) {
                instance ?: createDatabase(context).also {
                    instance = it
                }
            }

        private fun createDatabase(context: Context): KitabiDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                KitabiDatabase::class.java,
                DATABASE_NAME
            ).build()
    }
}

