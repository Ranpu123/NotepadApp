package com.example.notepad.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.notepad.data.local.source.dao.NotepadDao
import com.example.notepad.models.Folder
import com.example.notepad.models.Note

@Database(
    entities = [Folder::class, Note::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun notepadDao(): NotepadDao
}