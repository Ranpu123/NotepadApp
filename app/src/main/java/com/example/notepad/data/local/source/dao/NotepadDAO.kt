package com.example.notepad.data.local.source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.notepad.data.local.models.FolderWithNotes
import com.example.notepad.models.Folder
import com.example.notepad.models.Note
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
abstract class NotepadDao {

    @Transaction
    @Query("SELECT * FROM Folder WHERE folder_id = :folderId")
    abstract fun getFolderWithNotes(folderId: Int): Observable<FolderWithNotes>

    @Transaction
    @Query("SELECT * FROM Folder")
    abstract fun getAllFolders(): Observable<List<Folder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addFolder(folder: Folder): Single<Long>

    @Delete
    abstract fun removeFolder(folder: Folder): Completable

    @Transaction
    @Query ("UPDATE Folder SET pinned = 1 WHERE folder_id = :folderId")
    abstract fun pinFolder(folderId: Int): Completable

    @Transaction
    @Query ("UPDATE Folder SET pinned = 0 WHERE folder_id = :folderId")
    abstract fun unpinFolder(folderId: Int): Completable

    @Transaction
    @Query("SELECT * FROM Note WHERE note_id = :noteId")
    abstract fun getNoteById(noteId: Int) : Maybe<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addNote(note: Note): Single<Long>

    @Delete
    abstract fun removeNote(note: Note): Completable
}