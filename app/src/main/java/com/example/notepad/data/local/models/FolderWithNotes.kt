package com.example.notepad.data.local.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.notepad.models.Folder
import com.example.notepad.models.Note


data class FolderWithNotes(
    @Embedded val folder: Folder,
    @Relation(
        parentColumn = "folder_id",
        entityColumn = "folder_id"
    )
    val notes: List<Note>
) {
}