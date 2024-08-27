package com.example.notepad.data.local.database

import android.net.Uri
import androidx.core.net.toFile
import androidx.room.TypeConverter
import java.net.URI
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {

    @TypeConverter
    fun dateTimeFromTimestamp(value: String): LocalDateTime {
        return value.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
    }

    @TypeConverter
    fun dateTimeToTimestamp(value: LocalDateTime): String {
        return value.let { it.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
    }

    @TypeConverter
    fun uriToString(value: URI): String {
        return value.toString()
    }

    @TypeConverter
    fun stringToUri(value: String): URI {
        return URI(value)
    }

}