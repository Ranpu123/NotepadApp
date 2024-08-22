package com.example.notepad.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    indices = [
        Index(value = ["description"], unique = true)
    ]
)

data class Folder(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("folder_id")
    var id: Int,
    var description: String,
    var pinned: Boolean,
    @ColumnInfo("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),
) {

}