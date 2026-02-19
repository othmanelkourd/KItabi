package com.smile.kitabi.data.local.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

/**
 * Type converters for Room database to handle custom types
 */
class DateConverter {

    @TypeConverter
    fun localDateToString(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun stringToLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }
}

