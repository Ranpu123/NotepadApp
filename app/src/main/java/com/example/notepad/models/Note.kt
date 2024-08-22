package com.example.notepad.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.net.URI
import java.time.LocalDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Folder::class,
            parentColumns = ["folder_id"],
            childColumns = ["folder_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("note_id")
    var id: Int,
    var title: String,
    var uri: URI,
    var createdAt: LocalDateTime,
    @ColumnInfo("folder_id")
    var folderId: Int
) {

}
