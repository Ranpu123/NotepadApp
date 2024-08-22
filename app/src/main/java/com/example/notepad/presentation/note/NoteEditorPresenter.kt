package com.example.notepad.presentation.note

import android.content.Context
import android.util.Log
import com.example.notepad.data.repository.NotepadRepository
import com.example.notepad.models.Note
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

class NoteEditorPresenter(
    private val view: NoteEditorContract.View,
    private val repository: NotepadRepository,
    private val useCase: NoteEditorUseCase,
    private val context: Context
): NoteEditorContract.Presenter {

    override fun createNewNote(richText: String, title: String, folderId: Int) {
        view.showLoading()
        repository.addNote(folderId, title, richText, context.filesDir)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.showSuccessMessage("Note Created Successfully")
                view.backToList()
            },{
                view.hideLoading()
                view.showErrorMessage(it.message ?: "Error Creating New Note")
            })
    }

    override fun updateNoteData(richText: String, title: String, noteId: Int) {
        view.showLoading()
        Log.e("[NoteEditor]", title)
        repository.getNoteById(noteId)
            .doOnSuccess {
                it.title = title
                updateFile(it, richText)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({},{
                view.showErrorMessage(it.message ?: "Error Loading Note Data")
                view.hideLoading()
            })
    }

    private fun updateFile(note: Note, richText: String){
        view.showLoading()
        repository.updateFile(File(note.uri), richText)
            .andThen(repository.updateNote(note))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.showSuccessMessage("Note Updated Successfully")
                view.backToList()
            },{
                view.showErrorMessage(it.message ?: "Error Updating Note Data")
                view.hideLoading()
            })
    }

    override fun getNoteData(noteId: Int) {
        view.showLoading()
        repository.getNoteById(noteId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadNoteContent(it)
                view.hideLoading()
            },{
                view.showErrorMessage(it.message ?: "Error Loading Note Data")
                view.hideLoading()
            })
    }

    private fun loadNoteContent(note: Note){
        view.showTitle(note.title)
        view.showLoading()
        repository.readFile(File(note.uri))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.showRichText(it ?: "")
                view.hideLoading()
            },{
                view.showErrorMessage(it.message ?: "Error Loading Note Data")
                view.hideLoading()
            })
    }

    override fun validate(title: String) {
        view.disableSave()
        if(title.isNullOrBlank()){
            view.editTextError("Title cannot be empty")
            return
        }
        if(useCase.validate(title)){
            view.editTextError("Title can't be bigger than 255 characters")
            return
        }
        view.editTextError(null)
        view.enableSave()
    }
}