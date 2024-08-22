package com.example.notepad.data.repository

import com.example.notepad.data.local.models.FolderWithNotes
import com.example.notepad.data.local.source.INotepadFileSource
import com.example.notepad.data.local.source.INotepadFileSource.Companion.TEMP_CONTENT_NAME
import com.example.notepad.data.local.source.INotepadFileSource.Companion.TEMP_TITLE_NAME
import com.example.notepad.data.local.source.dao.NotepadDao
import com.example.notepad.models.Folder
import com.example.notepad.models.Note
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File
import java.time.LocalDateTime
import java.util.UUID

class NotepadRepository(
    private val dao: NotepadDao,
    private val fileSource: INotepadFileSource,
) {

    fun getFolderWithNotes(folderId: Int): Observable<FolderWithNotes> = dao.getFolderWithNotes(folderId)

    fun getAllFolders(): Observable<List<Folder>> = dao.getAllFolders()

    fun addFolder(
        description: String,
    ): Single<Long> {
        val folder = Folder(
            id = 0,
            description = description,
            createdAt = LocalDateTime.now(),
            pinned = false
        )
        return dao.addFolder(folder)
    }

    fun removeFolder(folder: Folder): Completable = dao.removeFolder(folder)

    fun pinFolder(folderId: Int): Completable = dao.pinFolder(folderId)

    fun unpinFolder(folderId: Int): Completable = dao.unpinFolder(folderId)

    fun getNoteById(noteId: Int): Maybe<Note> = dao.getNoteById(noteId)

    fun removeNote(note: Note): Completable = dao.removeNote(note)

    fun addNote(
        folderId: Int,
        title: String,
        content: String,
        contextPath: File
    ): Single<Long> {
        val note = Note(
            id = 0,
            folderId = folderId,
            title = title,
            createdAt = LocalDateTime.now(),
            uri = File(contextPath, title + UUID.randomUUID()).toURI(),
        )
        return dao.addNote(note).flatMap { id ->
            if(id > 0 && fileSource.writeToStorage(File(note.uri), content)){
                Single.just(id)
            } else {
                if (id > 0)
                    Single.defer{
                        dao.removeNote(note)
                            .andThen(Single.error(Exception("Failed to save note")))
                    }
                else
                    Single.error(Exception("Failed to save note"))
            }
        }
    }

    fun updateNote(
        note: Note,
    ): Single<Long> {
        return dao.addNote(note)
    }


    fun updateFile(file: File, content: String): Completable{
        return Completable.create{ emitter->
            try {
                if(fileSource.writeToStorage(file, content)){
                    emitter.onComplete()
                } else {
                    emitter.onError(Exception("Error updating file: ${file.name}."))
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    fun readFile(file: File): Single<String?>{
        return Single.create { emitter ->
            try {
                val content = fileSource.readFromStorage(file)
                if (content != null) {
                    emitter.onSuccess(content)
                } else {
                    emitter.onError(Exception("Error reading file: ${file.name}."))
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }
}